package Hoseo.GraduationProject.Admin.Lecture.Repository;

import Hoseo.GraduationProject.Admin.Lecture.Domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
