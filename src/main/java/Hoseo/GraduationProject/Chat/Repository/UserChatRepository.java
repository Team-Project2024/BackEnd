package Hoseo.GraduationProject.Chat.Repository;

import Hoseo.GraduationProject.Chat.Domain.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {
    List<UserChat> findByMemberId(String userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_chat WHERE member_id = :memberId", nativeQuery = true)
    void deleteUserChatByMemberId(@Param("memberId") String memberId);
}
