package Hoseo.GraduationProject.Chat.Service;

import Hoseo.GraduationProject.API.Lecture.Service.StudentLectureService;
import Hoseo.GraduationProject.API.SchoolLocation.Service.SchoolLocationService;
import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
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

    @Value("${ProjectId}")
    private String GoogleProjectId;

    @Value("${CredentialFileURL}")
    private String CredentialFileURL;

    private static final String LOCATION = "global";
    private static final String LANGUAGE_CODE = "ko";
    private static final String GOOGLE_SCOPED_URL = "https://www.googleapis.com/auth/cloud-platform";

    private final MakeAnswerService makeAnswerService;
    private final StudentLectureService studentLectureService;
    private final SchoolLocationService schoolLocationService;

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

            //DialogFlow에서 전달받은 것과 사용자의 ID를 전달
            ResponseDjangoDTO responseDjangoDTO = makeAnswerService.postDjango(queryResult, member.getId(), user_chat);

            SaveChatBotContent saveChatBotContent = new SaveChatBotContent();
            saveChatBotContent.setContent(responseDjangoDTO.getContent());
            saveChatBotContent.setTable(responseDjangoDTO.getTable());

            // Lecture, School_Location 외에는 Data를 받아올 필요가 없음(세부 정보를 넘길 필요 X)
            if (responseDjangoDTO.getTable().equals("lecture")) {
                saveChatBotContent.setData(studentLectureService.getLectureListDTO(responseDjangoDTO.getData()).toString());
            }
            else if(responseDjangoDTO.getTable().equals("school_location")){
                saveChatBotContent.setData(schoolLocationService.getSchoolLocationList(responseDjangoDTO.getData()).toString());
            }

            // SaveChatBotContent JSON을 String으로 변환하여 저장
            return saveChat(user_chat, roomId, saveChatBotContent.toString());
        } catch (Exception e){
            throw new BusinessLogicException(ChatExceptionType.CHAT_ERROR);
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