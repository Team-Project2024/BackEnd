package Hoseo.GraduationProject.API.Major.Repository;

import Hoseo.GraduationProject.API.Major.Domain.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
}
