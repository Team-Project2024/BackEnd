package Hoseo.GraduationProject.Chat.Domain;

import Hoseo.GraduationProject.Member.Domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "user_chat")
@NoArgsConstructor
public class UserChat {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "chat_date", nullable = false)
    private Timestamp chatDate;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @OneToOne(mappedBy = "userChat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ChatBot chatBot;

    @Builder
    public UserChat(Long id, Member member, Timestamp chatDate,
                    String content, Long roomId, ChatBot chatBot) {
        this.id = id;
        this.member = member;
        this.chatDate = chatDate;
        this.content = content;
        this.roomId = roomId;
        this.chatBot = chatBot;
    }
}
