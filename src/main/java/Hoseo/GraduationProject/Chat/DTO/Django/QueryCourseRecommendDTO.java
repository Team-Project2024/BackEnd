package Hoseo.GraduationProject.Chat.DTO.Django;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryCourseRecommendDTO {
    private String memberId;
    private boolean teamPlay;
    private String classMethod;
    private String credit;
    private String classification;
    private String testType;
    private String gradeMethod;
    private boolean aiSw;
}