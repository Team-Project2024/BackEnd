package Hoseo.GraduationProject.Chat.Repository;

import Hoseo.GraduationProject.Chat.Domain.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {
    List<UserChat> findByMemberId(String userId);
}
