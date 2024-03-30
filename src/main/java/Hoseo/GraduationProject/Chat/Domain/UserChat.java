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

    @OneToOne(mappedBy = "userChat", fetch = FetchType.EAGER)
    private ChatBot chatBot;

    @Builder
    public UserChat(Long id, Member member, Timestamp chatDate, String content) {
        this.id = id;
        this.member = member;
        this.chatDate = chatDate;
        this.content = content;
    }
}
