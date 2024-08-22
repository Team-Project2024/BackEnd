package Hoseo.GraduationProject.API.GraduationRequirements.Repository;

import Hoseo.GraduationProject.API.GraduationRequirements.Domain.GraduationRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GRRepository extends JpaRepository<GraduationRequirements, Long> {

    @Query("Select gr from GraduationRequirements gr join fetch gr.major where gr.year = :year And gr.major.majorId = :majorId")
    GraduationRequirements getGRByYearAndMajorId(String year, Long majorId);
}
