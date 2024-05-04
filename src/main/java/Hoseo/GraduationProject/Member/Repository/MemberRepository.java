package Hoseo.GraduationProject.Member.Repository;

import Hoseo.GraduationProject.Member.Domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Member findByEmailAndName(String email, String name);
    Member findByIdAndEmailAndName(String id, String email, String name);

    @Transactional
    @Modifying
    @Query("SELECT m FROM Member m JOIN FETCH m.major WHERE m.role = 'ROLE_PROFESSOR'")
    List<Member> findByRoleProfessor();
}
