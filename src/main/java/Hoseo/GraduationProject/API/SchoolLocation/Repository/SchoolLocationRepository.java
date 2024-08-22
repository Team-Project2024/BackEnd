package Hoseo.GraduationProject.API.SchoolLocation.Repository;

import Hoseo.GraduationProject.API.SchoolLocation.Domain.SchoolLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolLocationRepository extends JpaRepository<SchoolLocation, Long> {
}
