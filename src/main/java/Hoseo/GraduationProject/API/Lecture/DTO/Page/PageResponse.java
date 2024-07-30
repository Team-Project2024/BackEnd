package Hoseo.GraduationProject.API.Lecture.DTO.Page;

import Hoseo.GraduationProject.API.Lecture.DTO.Response.ResponseLectureDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse {
    private List<ResponseLectureDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
