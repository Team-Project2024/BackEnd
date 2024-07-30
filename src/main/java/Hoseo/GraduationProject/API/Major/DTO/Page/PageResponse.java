package Hoseo.GraduationProject.API.Major.DTO.Page;

import Hoseo.GraduationProject.API.Major.DTO.Response.ResponseMajorDTO;
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
    private List<ResponseMajorDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

