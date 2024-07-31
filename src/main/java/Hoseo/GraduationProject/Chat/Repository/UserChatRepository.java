package Hoseo.GraduationProject.Chat.Repository;

import Hoseo.GraduationProject.Chat.Domain.UserChat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_chat WHERE room_id = :chatRoomId", nativeQuery = true)
    void deleteByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_chat WHERE room_id IN (SELECT id FROM chat_room WHERE member_id = :memberId)", nativeQuery = true)
    void deleteByMemberId(@Param("memberId") String memberId);

    @Query("SELECT uc FROM UserChat uc JOIN FETCH uc.chatBot cb WHERE uc.chatRoom.id = :chatRoomId")
    List<UserChat> findByChatRoomId(Long chatRoomId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    UserChat save(UserChat userChat);
}
