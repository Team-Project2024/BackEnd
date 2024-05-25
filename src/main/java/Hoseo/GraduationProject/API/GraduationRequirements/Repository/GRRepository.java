package Hoseo.GraduationProject.API.GraduationRequirements.Repository;

import Hoseo.GraduationProject.API.GraduationRequirements.Domain.GraduationRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GRRepository extends JpaRepository<GraduationRequirements, Long> {
}
