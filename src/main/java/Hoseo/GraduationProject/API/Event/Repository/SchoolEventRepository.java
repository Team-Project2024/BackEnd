package Hoseo.GraduationProject.API.Event.Repository;

import Hoseo.GraduationProject.API.Event.Domain.SchoolEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolEventRepository extends JpaRepository<SchoolEvent, Long> {
}
