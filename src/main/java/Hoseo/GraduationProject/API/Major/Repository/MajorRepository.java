package Hoseo.GraduationProject.API.Major.Repository;

import Hoseo.GraduationProject.API.Major.Domain.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Page<Major> findAll(Pageable pageable);

    @Query("select m from Major m where m.department like %:keyword% or m.track like %:keyword%")
    Page<Major> findByKeywordContaining(@Param("keyword") String keyword, Pageable pageable);

}
