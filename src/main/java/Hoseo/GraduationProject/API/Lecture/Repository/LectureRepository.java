package Hoseo.GraduationProject.API.Lecture.Repository;

import Hoseo.GraduationProject.API.Lecture.Domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query("select l from Lecture l left join fetch l.member m left join fetch m.major")
    List<Lecture> findAllWithMemberAndMajor();

}
