package Hoseo.GraduationProject.Member.Repository;

import Hoseo.GraduationProject.Member.Domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Member findByEmailAndName(String email, String name);
    Member findByIdAndEmailAndName(String id, String email, String name);
}
