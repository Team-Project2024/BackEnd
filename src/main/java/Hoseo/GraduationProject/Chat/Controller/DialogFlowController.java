package Hoseo.GraduationProject.Chat.Controller;

import Hoseo.GraduationProject.Chat.DTO.DjangoTestDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatRoomDTO;
import Hoseo.GraduationProject.Chat.ExceptionType.ChatExceptionType;
import Hoseo.GraduationProject.Chat.Service.ChatService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2WebhookResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class DialogFlowController {

    @Value("${DjangoURL}")
    private String djangoUrl;

    private static final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

    private final ChatService chatService;

    // 채팅방 삭제하면서 유저 채팅과 챗봇 채팅 삭제

    @PostMapping("/chatRoom")
    public ResponseEntity<Long> createChatRoom(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChatRoom(member.getMember()));
    }

    @GetMapping("/chatRoom")
    public ResponseEntity<List<ResponseChatRoomDTO>> getChatRoomList(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.getChatRoomList(member.getMember()));
    }

    @DeleteMapping("/chatRoom")
    public ResponseEntity<Void> deleteChatRoom(@AuthenticationPrincipal CustomUserDetails member, @RequestParam Long chatRoomId) {
        chatService.deleteChatRoom(member.getMember(), chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
    * 채팅 메서드
    * */
    @PostMapping("/test")
    public ResponseEntity<String> testCreateChat(@AuthenticationPrincipal CustomUserDetails member, @RequestParam String message, @RequestParam Long chatRoomId){
        /**
         * 메시지를 받아서 dialogflow에 전송
         * dialogflow로부터 intent, entity를 리턴받음
         *
         * Django에 dialogflow로부터 intent를 기준으로 다른 엔드포인트에 요청을 보냄(리턴받은 entity와 사용자의 학번을 전송함)
         * 여기에서 데이터를 받는것은 우리가 정하면 되는데
         * {
         *  subject: "과목추천",
         *  content: [
         *      {pk:00x12, name:"소프트웨어 공학"},~,~,~
         *  ]
         * }
         * 이게 정배라고 생각함
         * --- 이 부분 마이크로 서비스에서 문장 형태 + pk 값으로 전달해준다니까 우선 대기 ---
         *
         * 그리고서 이걸 프론트에 보내주는게 문제인데
         * 1안. 채팅 형태로 채팅을 반환해준다.
         * 2안. 데이터만을 보내줘서 프론트에서 가공한다.
         * 사실은 챗봇에서 대답 형태로 맞는거같은데
         * 2안으로 가면 받은 데이터를 json으로 저장하고 pk에 있는 값을 모두 entity를 조회해서 entity별로 전송해줘야됨
         * 근데 여기에서 entity마다 변수값이 다르기 때문에 subject부분을 붙여서 전달하는게 필요함 이게 맞나 ㅋㅋ,,,
         */

        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.testCreateChat(member, message, chatRoomId));
    }

    /**
     * 채팅 내역을 가져오는 메서드
     * */
    @GetMapping
    public ResponseEntity<ResponseChatDTO> getChat(@RequestParam Long chatRoomId){
        ResponseChatDTO responseChatDTO = chatService.getChat(chatRoomId);
        if(responseChatDTO.getUserChat().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseChatDTO);
        } else{
            return ResponseEntity.status(HttpStatus.OK).body(responseChatDTO);
        }
    }

    /**
     * dialog Flow를 통해 질문의 인텐트를 분석해서 인텐트와 엔티티를 받아올거임
     * */
    @PostMapping
    public ResponseEntity<?> dialogFlowWebHook(@RequestBody String requestStr, HttpServletRequest servletRequest) throws IOException {
        System.out.println(requestStr);
        try {
            GoogleCloudDialogflowV2WebhookResponse response = new GoogleCloudDialogflowV2WebhookResponse(); // response 객체
            GoogleCloudDialogflowV2WebhookRequest request =
                    jacksonFactory.createJsonParser(requestStr).parse(GoogleCloudDialogflowV2WebhookRequest.class); // request 객체에서 파싱

            Map<String,Object> params = request.getQueryResult().getParameters(); // 파라미터 받아서 map에다 저장

            if (!params.isEmpty()) {
                System.out.println(params);
                response.setFulfillmentText("다음과 같은 파라미터가 나왔습니다 " + params.toString());
            }
            else {
                response.setFulfillmentText("전송에 실패하였습니다.");
            }

            return new ResponseEntity<GoogleCloudDialogflowV2WebhookResponse>(response, HttpStatus.OK);
        }
        catch (Exception ex) {
            throw new BusinessLogicException(ChatExceptionType.CHAT_ERROR);
        }
    }

    /**
     * Django로 데이터를 주고 받는다. 모든 요청들이 Django로 연동 될 것이고 인텐트, 엔티티(리스트), 사용자 정보를 넘겨줄거임
     * */
    @PostMapping("/django")
    public ResponseEntity<Void> djangoTest(){
        WebClient webClient = WebClient.create(djangoUrl);

        DjangoTestDTO djangoTestDTO = new DjangoTestDTO();
        djangoTestDTO.setContent("aa");

        webClient.post()
                .uri("/chat/")
                .body(BodyInserters.fromValue(djangoTestDTO))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> System.out.println("Response from Django: " + response));

        return ResponseEntity.status(HttpStatus.OK).build();
    }


//    @RequestMapping("/detectIntentTexts")
//    @PostMapping
//    public String detectIntentTexts(@RequestBody ChatRequest chatRequest) {
//        String projectId = chatRequest.getProjectId();
//        String text = chatRequest.getText();
//        String sessionId = chatRequest.getSessionId();
//        String languageCode = chatRequest.getLanguageCode();
//
//        // Instantiates a client
//        QueryResult queryResult = null;
//        try (SessionsClient sessionsClient = SessionsClient.create()) {
//            // Set the session name using the sessionId (UUID) and projectID (my-project-id)
//            SessionName session = SessionName.of(projectId, sessionId);
//            System.out.println("Session Path: " + session.toString());
//
//            // Detect intents for each text input
//            // Set the text (hello) and language code (en-US) for the query
//            Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);
//
//            // Build the query with the TextInput
//            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
//
//            // Performs the detect intent request
//            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
//
//            // Display the query result
//            queryResult = response.getQueryResult();
//
//            System.out.println("====================");
//            System.out.println(String.format("Query Text: '%s'\n", queryResult.getQueryText()));
//            System.out.println(String.format("Detected Intent: %s (confidence: %f)\n",
//                    queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence()));
//            System.out.println(String.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText()));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return String.format("'%s'", queryResult.getFulfillmentText());
//    }
}
