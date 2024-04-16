package Hoseo.GraduationProject.Chat.Controller;

import Hoseo.GraduationProject.Chat.DTO.ChatBotDTO;
import Hoseo.GraduationProject.Chat.DTO.DjangoTestDTO;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatDTO;
import Hoseo.GraduationProject.Chat.ExceptionType.ChatExceptionType;
import Hoseo.GraduationProject.Chat.Service.ChatService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import com.google.api.Http;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2WebhookResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class DialogFlowController {

    private static JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

    private final ChatService chatService;

    @PostMapping("/test")
    public ResponseEntity<String> testCreateChat(@AuthenticationPrincipal CustomUserDetails member, @RequestParam String message){
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.testCreateChat(member.getId(),message));
    }

    @GetMapping
    public ResponseEntity<ResponseChatDTO> getChat(@AuthenticationPrincipal CustomUserDetails member){
        ResponseChatDTO responseChatDTO = chatService.getChat(member.getId());
        if(responseChatDTO.getUserChat().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseChatDTO);
        } else{
            return ResponseEntity.status(HttpStatus.OK).body(responseChatDTO);
        }
    }

    @PostMapping
    public ResponseEntity<?> dialogFlowWebHook(@RequestBody String requestStr, HttpServletRequest servletRequest) throws IOException {
        System.out.println(requestStr);
        try {
            GoogleCloudDialogflowV2WebhookResponse response = new GoogleCloudDialogflowV2WebhookResponse(); // response 객체
            GoogleCloudDialogflowV2WebhookRequest request =
                    jacksonFactory.createJsonParser(requestStr).parse(GoogleCloudDialogflowV2WebhookRequest.class); // request 객체에서 파싱

            Map<String,Object> params = request.getQueryResult().getParameters(); // 파라미터 받아서 map에다 저장

            if (params.size() > 0) {
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

    @PostMapping("/django")
    public ResponseEntity<Void> djangoTest(){
        WebClient webClient = WebClient.create("http://127.0.0.1:8000");

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

    @DeleteMapping
    public ResponseEntity<Void> deleteChat(@AuthenticationPrincipal CustomUserDetails member){
        chatService.deleteChat(member.getId());
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
