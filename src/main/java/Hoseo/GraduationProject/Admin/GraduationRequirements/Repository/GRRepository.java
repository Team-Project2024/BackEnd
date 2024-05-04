package Hoseo.GraduationProject.Admin.GraduationRequirements.Repository;

import Hoseo.GraduationProject.Admin.GraduationRequirements.Domain.GraduationRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GRRepository extends JpaRepository<GraduationRequirements, Long> {
}
