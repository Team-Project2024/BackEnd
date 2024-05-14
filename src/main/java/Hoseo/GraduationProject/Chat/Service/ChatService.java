package Hoseo.GraduationProject.Chat.Service;

import Hoseo.GraduationProject.API.Event.Service.StudentEventService;
import Hoseo.GraduationProject.API.Lecture.Service.StudentLectureService;
import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.Django.QueryCourseRecommendDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseDjangoDTO;
import Hoseo.GraduationProject.Chat.DTO.Django.SaveChatBotContent;
import Hoseo.GraduationProject.Chat.DTO.Django.UnivEventDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatDTO;
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
@RequiredArgsConstructor
public class ChatService {

    @Value("${DjangoURL}")
    private String djangoUrl;

    @Value("${ProjectId}")
    private String projectId;

    @Value("${CredentialFileURL}")
    private String CredentialFileURL;

    private static final String LOCATION = "global";
    private static final String LANGUAGE_CODE = "ko";
    private static final String GOOGLE_SCOPED_URL = "https://www.googleapis.com/auth/cloud-platform";

    private final StudentLectureService studentLectureService;
    private final StudentEventService studentEventService;

    private final UserChatRepository userChatRepository;
    private final ChatBotRepository chatBotRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
    * 특정 채팅방의 모든 채팅을 반환하는 메서드
    */
    @Transactional(readOnly = true)
    public ResponseChatDTO getChat(Long chatRoomId){
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
     * Dialog Flow에
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

        SessionsClient sessionsClient = SessionsClient.create(sessionsSettings);
        String sessionId = UUID.randomUUID().toString();
        SessionName session = SessionName.ofProjectLocationSessionName(projectId, LOCATION, sessionId);

        TextInput.Builder textInput =
                TextInput.newBuilder().setText(user_chat).setLanguageCode(LANGUAGE_CODE);
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        // Dialog flow의 요청을 받는 변수
        DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
        // 사용할 데이터를 분리
        QueryResult queryResult = response.getQueryResult();

        /**
         * Django로 부터 값을 받아옴
         * 반환 값 - content, table, [pk]
         */
        ResponseDjangoDTO responseDjangoDTO = postDjango(queryResult, member.getId());

        /**
         * table에 맞게 service에다 PK 리스트로 전달해서 DTO로 변환 해서 받아오면 다 다른 DTO가 됨 아닌가? content에 제네릭으로 Object 받으면 안됨?
         *
         */
        if(!responseDjangoDTO.getContent().isEmpty()){
            SaveChatBotContent saveChatBotContent = new SaveChatBotContent();
            saveChatBotContent.setContent(responseDjangoDTO.getContent());
            saveChatBotContent.setTable(responseDjangoDTO.getTable());

            /**
             * table에 따라 다른 service 레이어에서 데이터를 가져옴
             * 이 부분에서는 getContent는 비어있지 않지만 table이 비어있는 DialogFlow에서 바로 답변이 오는 부분은
             * 따로 data를 가져올 필요가 없기 때문에 처리하지 않음
             * 졸업요건도 data부분이 비어서 오기 때문에 함께 처리
             */
            if(responseDjangoDTO.getTable().equals("lecture")){
                saveChatBotContent.setData(studentLectureService.getLectureListDTO(responseDjangoDTO.getData()).toString());
                System.out.println("lecture = " + studentLectureService.getLectureListDTO(responseDjangoDTO.getData()).toString());
            } else if(responseDjangoDTO.getTable().equals("school_event")){
                saveChatBotContent.setData(studentEventService.getEventInfoList(responseDjangoDTO.getData()).toString());
                System.out.println("event = " + studentEventService.getEventInfoList(responseDjangoDTO.getData()).toString());
            }
            return saveChat(user_chat,roomId, saveChatBotContent.toString());
        } else{
            // 예외처리
            throw new BusinessLogicException(ChatExceptionType.CHAT_ERROR);
        }
    }

    /**
     * Dialog flow로부터 받은 데이터를 Django에 있는 추천시스템으로 보내여 챗봇의 답변과 데이터를 긁어온 테이블, PK값을 리스트로 전달받음
     * 만약 학사 관련 데이터가 아닐 경우에는 Django로 데이터를 보내지 않고 Dialog flow로 부터 바로 답변이 옴
     */
    private ResponseDjangoDTO postDjango(QueryResult queryResult, String memberId){
        String intent = queryResult.getIntent().getDisplayName();
        WebClient webClient = WebClient.create(djangoUrl);

        if(intent.equals("QueryCourseRecommend")){ // 질문 기반 과목 추천
            String classification = queryResult.getParameters().getFieldsMap().get("classification").getListValue().getValuesList().get(0).getStringValue();
            String credit = queryResult.getParameters().getFieldsMap().get("credit").getStringValue();
            String classMethod = queryResult.getParameters().getFieldsMap().get("classmethod").getStringValue();
            String testType = queryResult.getParameters().getFieldsMap().get("testType").getStringValue();

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
                    .block();
        }
        else if(intent.equals("HistoryCourseRecommend")){ // 수강 기록 기반 과목 추천
            return webClient.post()
                    .uri("/chat/course/history-recommend/")
                    .body(BodyInserters.fromValue(memberId))
                    .retrieve()
                    .bodyToMono(ResponseDjangoDTO.class)
                    .block();
        }
        else if(intent.equals("graduationCheck")){ // 졸업요건 조회
            return webClient.post()
                    .uri("/chat/course/graduation-check/")
                    .body(BodyInserters.fromValue(memberId))
                    .retrieve()
                    .bodyToMono(ResponseDjangoDTO.class)
                    .block();
        }
        else if(intent.equals("UniEvent")){ // 학교 행사 조회
            String month = queryResult.getParameters().getFieldsMap().get("month").getStringValue();

            UnivEventDTO univEventDTO = new UnivEventDTO();
            univEventDTO.setMemberId(memberId);
            //month 데이터가 어디에 담겨서 오는지를 모르겠음
            univEventDTO.setMonth(month);

            return webClient.post()
                    .uri("/chat/univ-event/")
                    //여기에서는 month 랑 memberId를 같이 보내줘야됨
                    .body(BodyInserters.fromValue(univEventDTO))
                    .retrieve()
                    .bodyToMono(ResponseDjangoDTO.class)
                    .block();
        } else {
            // 이 부분은 DialogFlow에서 받은 문장을 보내주면 됨 Django로 넘어가지 않고 답변 주는 형태임
            System.out.println("해당 없음");
        }
        return new ResponseDjangoDTO();
    }

    private ChatBotDTO saveChat(String user_chat, Long chatRoomId, String chatbot_chat){
        System.out.println(chatbot_chat);
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
            chatRoomRepository.save(chatroom);
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