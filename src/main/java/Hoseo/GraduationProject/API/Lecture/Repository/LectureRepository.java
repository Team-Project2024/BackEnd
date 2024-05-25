package Hoseo.GraduationProject.API.Lecture.Repository;

import Hoseo.GraduationProject.API.Lecture.Domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query("select l from Lecture l left join fetch l.member m left join fetch m.major left join fetch m.confirmCompletion")
    List<Lecture> findAllWithMemberAndMajor();

    @Query("select l from Lecture l left join fetch l.member m left join fetch m.major left join fetch m.confirmCompletion where l.id in :ids")
    List<Lecture> findAllByIds(@Param("ids") List<Long> ids);
}
