package Hoseo.GraduationProject.API.Lecture.Repository;

import Hoseo.GraduationProject.API.Lecture.Domain.LectureDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureDetailRepository extends JpaRepository<LectureDetail, Long> {
    List<LectureDetail> findAllByLectureId(Long lectureId);
}
