package Hoseo.GraduationProject.Chat.Service;

import Hoseo.GraduationProject.API.Event.DTO.ExDTO;
import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.Django.QueryCourseRecommendDTO;
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

    private final UserChatRepository userChatRepository;
    private final ChatBotRepository chatBotRepository;
    private final ChatRoomRepository chatRoomRepository;

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

    @Transactional(rollbackFor = Exception.class)
    public String detectIntentWithLocation(String user_chat,Long roomId, Member member) throws IOException, ApiException {
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

        DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
        QueryResult queryResult = response.getQueryResult();

        //postDjango에서 가져오는 고정이어야됨 content, table, [pk] 이렇게 가져오는게 제일 베스트
        postDjango(queryResult, member.getId());

        String chatBot_chat = "챗봇에 얻은 본문";
//        testCreateChat(user_chat, roomId, chatBot_chat);

        return queryResult.getIntent().getDisplayName(); //현재는 아무꺼나
    }

    private String testCreateChat(String user_chat, Long chatRoomId, String chatbot_chat){
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
        } catch (Exception e){
            throw new BusinessLogicException(ChatExceptionType.SAVE_CHAT_ERROR);
        }
        return chatbot_chat;
    }

    //postDjango에서 반환하는 값이 같아야됨 다시 돌아가서 데이터 가져오는게 나을듯
    private void postDjango(QueryResult queryResult, String memberId){
        String intent = queryResult.getIntent().getDisplayName();
        WebClient webClient = WebClient.create(djangoUrl);
        System.out.println(intent);

        if(intent.equals("QueryCourseRecommend")){ // 질문 기반 과목 추천
            String classification = queryResult.getParameters().getFieldsMap().get("classification").getListValue().getValuesList().get(0).getStringValue();
            String teamPlay = queryResult.getParameters().getFieldsMap().get("teamplay").getStringValue();
            String credit = queryResult.getParameters().getFieldsMap().get("credit").getStringValue();
            String classMethod = queryResult.getParameters().getFieldsMap().get("classmethod").getStringValue();
            String testType = queryResult.getParameters().getFieldsMap().get("testType").getStringValue();

            QueryCourseRecommendDTO queryCourseRecommendDTO = new QueryCourseRecommendDTO();
            queryCourseRecommendDTO.setMemberId(memberId);
            queryCourseRecommendDTO.setClassification(classification);
            queryCourseRecommendDTO.setTeamPlay(Boolean.parseBoolean(teamPlay));
            queryCourseRecommendDTO.setCredit(credit);
            queryCourseRecommendDTO.setClassMethod(classMethod);
            queryCourseRecommendDTO.setTestType(testType);

            // "0" or "1" 로 오는 String을 "1"과 같다면 true를 아니라면 false로 초기화
            System.out.println(queryResult.getParameters());
            queryCourseRecommendDTO.setAiSw("1".equals(queryResult.getParameters().getFieldsMap().get("aiSw").getStringValue()));

            webClient.post()
                    .uri("/chat/course/query-recommend/")
                    .body(BodyInserters.fromValue(queryCourseRecommendDTO))
                    .retrieve()
                    // 이부분 String을 DTO로 변경 필요함
                    .bodyToMono(ExDTO.class)
                    .subscribe(response -> System.out.println("Response from Django: " + response.getMemberId()));
        } else if(intent.equals("HistoryCourseRecommend")){ // 수강 기록 기반 과목 추천
            webClient.post()
                    .uri("/chat/course/history-recommend/")
                    .body(BodyInserters.fromValue(memberId))
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(response -> System.out.println("Response from Django: " + response));
        } else if(intent.equals("graduationCheck")){ // 졸업요건 조회
            webClient.post()
                    .uri("/chat/course/graduation-check/")
                    .body(BodyInserters.fromValue(memberId))
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(response -> System.out.println("Response from Django: " + response));
        } else if(intent.equals("UniEvent")){ // 학교 행사 조회
            String month = queryResult.getParameters().getFieldsMap().get("month").getStringValue();

            UnivEventDTO univEventDTO = new UnivEventDTO();
            univEventDTO.setMemberId(memberId);
            //month 데이터가 어디에 담겨서 오는지를 모르겠음
            univEventDTO.setMonth(month);

            webClient.post()
                    .uri("/chat/univ-event/")
                    //여기에서는 month 랑 memberId를 같이 보내줘야됨
                    .body(BodyInserters.fromValue(univEventDTO))
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(response -> System.out.println("Response from Django: " + response));
        } else {
            //에러 반환 ㄱㄱ
            System.out.println("해당 없음");
        }
    }
}
