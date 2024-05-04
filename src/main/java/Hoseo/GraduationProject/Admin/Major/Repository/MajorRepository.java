package Hoseo.GraduationProject.Admin.Major.Repository;

import Hoseo.GraduationProject.Admin.Major.Domain.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
}
