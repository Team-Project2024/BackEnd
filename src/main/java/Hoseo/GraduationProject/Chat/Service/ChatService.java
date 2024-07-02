package Hoseo.GraduationProject.Chat.Service;

import Hoseo.GraduationProject.API.Lecture.Service.StudentLectureService;
import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.Django.QueryCourseRecommendDTO;
import Hoseo.GraduationProject.Chat.DTO.Django.SaveChatBotContent;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseDjangoDTO;
import Hoseo.GraduationProject.Chat.DTO.UserChatDTO;
import Hoseo.GraduationProject.Chat.Domain.ChatBot;
import Hoseo.GraduationProject.Chat.Domain.ChatRoom;
import Hoseo.GraduationProject.Chat.Domain.UserChat;
import Hoseo.GraduationProject.Chat.ExceptionType.ChatExceptionType;
import Hoseo.GraduationProject.Chat.ExceptionType.ChatRoomExceptionType;
import Hoseo.GraduationProject.Chat.Repository.ChatBotRepository;
import Hoseo.GraduationProject.Chat.Repository.ChatRoomRepository;
import Hoseo.GraduationProject.Chat.Repository.UserChatRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.Domain.Member;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    @Value("${DjangoURL}")
    private String djangoUrl;

    @Value("${ProjectId}")
    private String GoogleProjectId;

    @Value("${CredentialFileURL}")
    private String CredentialFileURL;

    private static final String LOCATION = "global";
    private static final String LANGUAGE_CODE = "ko";
    private static final String GOOGLE_SCOPED_URL = "https://www.googleapis.com/auth/cloud-platform";

    private final StudentLectureService studentLectureService;

    private final UserChatRepository userChatRepository;
    private final ChatBotRepository chatBotRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 특정 채팅방의 모든 채팅을 반환하는 메서드
     */
    @Transactional(readOnly = true)
    public ResponseChatDTO getChat(Long chatRoomId) {
        List<UserChat> userChats = userChatRepository.findByChatRoomId(chatRoomId);

        // Response DTO를 생성하여 필요한 정보를 매핑합니다.
        ResponseChatDTO responseChatDTO = new ResponseChatDTO();

        // ResponseChatDTO에 들어갈 List 변수 초기화
        List<UserChatDTO> userChatDTOS = new ArrayList<>();
        List<ChatBotDTO> chatBotDTOS = new ArrayList<>();

        for (UserChat userChat : userChats) {
            UserChatDTO userChatDTO = new UserChatDTO();
            ChatBotDTO chatBotDTO = new ChatBotDTO();

            //UserChat에 있는 ChatBot의 답장 내역을 가져옴
            ChatBot chatBot = userChat.getChatBot();

            //UserChatDTO 설정
            userChatDTO.setId(userChat.getId());
            userChatDTO.setContent(userChat.getContent());
            userChatDTO.setChatDate(userChat.getChatDate());

            //ChatBotDTO 설정
            chatBotDTO.setId(chatBot.getId());
            chatBotDTO.setContent(chatBot.getContent());

            //List에 add
            userChatDTOS.add(userChatDTO);
            chatBotDTOS.add(chatBotDTO);
        }
        responseChatDTO.setUserChat(userChatDTOS);
        responseChatDTO.setChatBot(chatBotDTOS);

        return responseChatDTO;
    }

    /**
     * 사용자의 채팅 요청이 들어오면 챗봇의 대답 생성을 위한 메서드
     */
    @Transactional(rollbackFor = Exception.class)
    public ChatBotDTO detectIntentWithLocation(String user_chat,Long roomId, Member member) throws IOException, ApiException {
        //Google 사용자 권한 인증
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/" + CredentialFileURL))
                .createScoped(GOOGLE_SCOPED_URL);

        SessionsSettings sessionsSettings =
                SessionsSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

        try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)) {
            String sessionId = UUID.randomUUID().toString();
            SessionName session = SessionName.ofProjectLocationSessionName(GoogleProjectId, LOCATION, sessionId);

            TextInput.Builder textInput =
                    TextInput.newBuilder().setText(user_chat).setLanguageCode(LANGUAGE_CODE);
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            // Dialog flow의 요청을 받는 변수
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            // 사용할 데이터를 분리
            QueryResult queryResult = response.getQueryResult();

            ResponseDjangoDTO responseDjangoDTO = postDjango(queryResult, member.getId());

            SaveChatBotContent saveChatBotContent = new SaveChatBotContent();
            saveChatBotContent.setContent(responseDjangoDTO.getContent());
            saveChatBotContent.setTable(responseDjangoDTO.getTable());

            // Lecture, School_Event 외에는 Data를 받아올 필요가 없음(세부 정보를 넘길 필요 X)
            if (responseDjangoDTO.getTable().equals("lecture")) {
                saveChatBotContent.setData(studentLectureService.getLectureListDTO(responseDjangoDTO.getData()).toString());
            }

            // SaveChatBotContent JSON을 String으로 변환하여 저장
            return saveChat(user_chat, roomId, saveChatBotContent.toString());
        } catch (Exception e){
            throw new BusinessLogicException(ChatExceptionType.CHAT_ERROR);
        }
    }

    /**
     * Dialog flow로부터 받은 데이터를 Django에 있는 추천시스템으로 보내여 챗봇의 답변과 데이터를 긁어온 테이블, PK값을 리스트로 전달받음
     * 만약 학사 관련 데이터가 아닐 경우에는 Django로 데이터를 보내지 않고 Dialog flow로 부터 바로 답변이 옴
     */
    private ResponseDjangoDTO postDjango(QueryResult queryResult, String memberId){
        String intent = queryResult.getIntent().getDisplayName();
        log.info("ChatBot Intent : {}", intent);

        WebClient webClient = WebClient.create(djangoUrl);

        switch (intent) {
            case "QueryCourseRecommend" -> {
                String classification = "";
                if(!queryResult.getParameters().getFieldsMap().get("classification").getListValue().getValuesList().isEmpty()){
                    classification = queryResult.getParameters().getFieldsMap().get("classification").getListValue().getValuesList().get(0).getStringValue();
                }
                String credit = queryResult.getParameters().getFieldsMap().get("credit").getStringValue();
                String classMethod = queryResult.getParameters().getFieldsMap().get("classmethod").getStringValue();
                String testType = queryResult.getParameters().getFieldsMap().get("testType").getStringValue();

                log.info("classification : {}, credit : {}, classMethod, : {}, testType : {}", classification, credit, classMethod, testType);

                QueryCourseRecommendDTO queryCourseRecommendDTO = new QueryCourseRecommendDTO();
                queryCourseRecommendDTO.setMemberId(memberId);
                queryCourseRecommendDTO.setClassification(classification);
                queryCourseRecommendDTO.setCredit(credit);
                queryCourseRecommendDTO.setClassMethod(classMethod);
                queryCourseRecommendDTO.setTestType(testType);

                // "0" or "1" 로 오는 String을 "1"과 같다면 true를 아니라면 false로 초기화
                queryCourseRecommendDTO.setTeamPlay("1".equals(queryResult.getParameters().getFieldsMap().get("teamplay").getStringValue()));
                queryCourseRecommendDTO.setAiSw("1".equals(queryResult.getParameters().getFieldsMap().get("aiSw").getStringValue()));

                return webClient.post()
                        .uri("/chat/course/query-recommend/")
                        .body(BodyInserters.fromValue(queryCourseRecommendDTO))
                        .retrieve()
                        // 이부분 String을 DTO로 변경 필요함
                        .bodyToMono(ResponseDjangoDTO.class)
                        .block();  // 질문 기반 과목 추천
            }
            case "HistoryCourseRecommend" -> {
                return webClient.post()
                        .uri("/chat/course/history-recommend/")
                        .body(BodyInserters.fromValue(memberId))
                        .retrieve()
                        .bodyToMono(ResponseDjangoDTO.class)
                        .block();  // 수강 기록 기반 과목 추천
            }
            case "graduationCheck" -> {
                return webClient.post()
                        .uri("/chat/course/graduation-check/")
                        .body(BodyInserters.fromValue(memberId))
                        .retrieve()
                        .bodyToMono(ResponseDjangoDTO.class)
                        .block();  // 졸업요건 조회
            }
            //default
            default -> {
                ResponseDjangoDTO responseDjangoDTO = new ResponseDjangoDTO();
                responseDjangoDTO.setContent(queryResult.getFulfillmentText());
                responseDjangoDTO.setTable("");
                return responseDjangoDTO;
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ChatBotDTO saveChat(String user_chat, Long chatRoomId, String chatbot_chat) {
        ChatRoom chatroom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new BusinessLogicException(ChatRoomExceptionType.NOT_FOUND_CHATROOM));
        chatroom.updateLastChatDate(new Timestamp(System.currentTimeMillis()));

        UserChat userChat = UserChat.builder()
                .chatDate(new Timestamp(System.currentTimeMillis()))
                .chatRoom(chatroom)
                .content(user_chat)
                .build();

        ChatBot chatBot = ChatBot.builder()
                .content(chatbot_chat)
                .userChat(userChat)
                .build();

        try{
            userChatRepository.save(userChat);
            chatBotRepository.save(chatBot);
            ChatBotDTO chatBotDTO = new ChatBotDTO();
            chatBotDTO.setId(chatBot.getId());
            chatBotDTO.setContent(chatbot_chat);
            return chatBotDTO;
        } catch (Exception e){
            throw new BusinessLogicException(ChatExceptionType.SAVE_CHAT_ERROR);
        }
    }
}