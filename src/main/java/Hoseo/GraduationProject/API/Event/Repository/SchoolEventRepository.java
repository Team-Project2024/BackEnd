package Hoseo.GraduationProject.API.Event.Repository;

import Hoseo.GraduationProject.API.Event.Domain.SchoolEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolEventRepository extends JpaRepository<SchoolEvent, Long> {
    @Query("select sc from SchoolEvent sc where sc.id in :ids")
    List<SchoolEvent> findAllByIds(@Param("ids") List<Long> ids);
}
