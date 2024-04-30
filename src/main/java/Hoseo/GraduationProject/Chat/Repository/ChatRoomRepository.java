package Hoseo.GraduationProject.Chat.Repository;

import Hoseo.GraduationProject.Chat.Domain.ChatRoom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByMemberId(String memberId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM chat_room WHERE id = :id AND member_id = :memberId", nativeQuery = true)
    void deleteByIdAndMemberId(@Param("id") Long id, @Param("memberId") String memberId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM chat_room WHERE member_id = :memberId", nativeQuery = true)
    void deleteAllByMemberId(String memberId);
}
