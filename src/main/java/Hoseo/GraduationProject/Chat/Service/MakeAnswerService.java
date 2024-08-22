package Hoseo.GraduationProject.Chat.Service;


import Hoseo.GraduationProject.Chat.DTO.Django.QueryCourseRecommendDTO;
import Hoseo.GraduationProject.Chat.DTO.Django.QueryHoseoLocationDTO;
import Hoseo.GraduationProject.Chat.DTO.GPT.GPTRequest;
import Hoseo.GraduationProject.Chat.DTO.GPT.GPTResponse;
import Hoseo.GraduationProject.Chat.DTO.Response.ResponseDjangoDTO;
import com.google.cloud.dialogflow.v2.QueryResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakeAnswerService {

    @Value("${DjangoURL}")
    private String djangoUrl;

    @Value("${GPTModel}")
    private String model;

    @Value("${GPT_API_URL}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public ResponseDjangoDTO postDjango(QueryResult queryResult, String memberId, String userChat){
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
            //엔티티 뽑아서 같이 보내줘야됨
            case "hoseoLocation" -> {
                String uniInfoPlace = queryResult.getParameters().getFieldsMap().get("uniinfoplace").getStringValue();
                String dormitory = queryResult.getParameters().getFieldsMap().get("dormitory").getStringValue();
                String lectureRoom = queryResult.getParameters().getFieldsMap().get("lectureroom").getStringValue();
                String facilities = queryResult.getParameters().getFieldsMap().get("facilities").getStringValue();
                String eatPlace = queryResult.getParameters().getFieldsMap().get("eatPlace").getStringValue();

                QueryHoseoLocationDTO queryHoseoLocationDTO = new QueryHoseoLocationDTO();
                queryHoseoLocationDTO.setUniInfoPlace(uniInfoPlace);
                queryHoseoLocationDTO.setDormitory(dormitory);
                queryHoseoLocationDTO.setLectureRoom(lectureRoom);
                queryHoseoLocationDTO.setFacilities(facilities);
                queryHoseoLocationDTO.setEatPlace(eatPlace);

                return webClient.post()
                        .uri("chat/hoseo/location/")
                        .body(BodyInserters.fromValue(queryHoseoLocationDTO))
                        .retrieve()
                        .bodyToMono(ResponseDjangoDTO.class)
                        .block();  // 졸업요건 조회
            }
            // 기본적인 질문들은 ChatGPT로 질문을 넘길 예정임
            default -> {
                ResponseDjangoDTO responseDjangoDTO = new ResponseDjangoDTO();
                try{
                    GPTRequest request = new GPTRequest(
                            model,userChat,1,256,1,2,2);
                    System.out.println(request);
                    GPTResponse gptResponse = restTemplate.postForObject(
                            apiUrl
                            , request
                            , GPTResponse.class
                    );
                    System.out.println(gptResponse);
                    responseDjangoDTO.setContent(gptResponse.getChoices().get(0).getMessage().getContent());
                } catch(Exception e){
                    responseDjangoDTO.setContent("답변을 준비하지 못했습니다.");
                }
                responseDjangoDTO.setTable("");
                return responseDjangoDTO;
            }
        }
    }
}
