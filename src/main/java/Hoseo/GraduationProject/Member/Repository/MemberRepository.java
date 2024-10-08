package Hoseo.GraduationProject.Member.Repository;

import Hoseo.GraduationProject.Member.Domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Member findByEmailAndName(String email, String name);
    Member findByIdAndEmailAndName(String id, String email, String name);

    @Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.confirmCompletion WHERE m.role = :role")
    List<Member> findByRole(String role);

    @Query("SELECT m FROM Member m JOIN FETCH m.major")
    List<Member> find();
}
