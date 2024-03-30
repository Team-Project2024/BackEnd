package Hoseo.GraduationProject.Chat.Repository;

import Hoseo.GraduationProject.Chat.Domain.ChatBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ChatBotRepository extends JpaRepository<ChatBot, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM chat_bot WHERE user_chat_id IN (SELECT id FROM user_chat WHERE member_id = :memberId)", nativeQuery = true)
    void deleteChatBotByMemberId(@Param("memberId") String memberId);
}
