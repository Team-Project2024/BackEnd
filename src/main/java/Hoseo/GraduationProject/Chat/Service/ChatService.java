package Hoseo.GraduationProject.Chat.Service;

import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.DjangoTestDTO;
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

        // 여기에 Intent? 전달해줘 안에 값들 변경 필요함
        //queryResult -> parameter를 Django로 전달해주고
        //queryResult -> queryText는 user_chat으로 저장
        // Django에서 받아오는 chatbot_chat의 채팅은 chat_bot에 저장
        // postDjango();
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

    private void postDjango(DjangoTestDTO djangoTestDTO){
        WebClient webClient = WebClient.create(djangoUrl);

        webClient.post()
                .uri("/chat/")
                .body(BodyInserters.fromValue(djangoTestDTO))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> System.out.println("Response from Django: " + response));
    }
}
