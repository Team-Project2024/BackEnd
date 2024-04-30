package Hoseo.GraduationProject.Chat.Domain;

import Hoseo.GraduationProject.Member.Domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "chat_room")
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "last_chat_date")
    private Timestamp lastChatDate;

    @Column(name = "first_chat")
    private String firstChat;

    public void updateLastChatDate(Timestamp lastChatDate) {
        this.lastChatDate = lastChatDate;
    }

    @Builder
    ChatRoom(Member member, Timestamp lastChatDate, String firstChat) {
        this.member = member;
        this.lastChatDate = lastChatDate;
        this.firstChat = firstChat;
    }
}
