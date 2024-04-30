package Hoseo.GraduationProject.Chat.Domain;

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

    @Column(name = "chat_date", nullable = false)
    private Timestamp chatDate;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @OneToOne(mappedBy = "userChat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ChatBot chatBot;

    @Builder
    public UserChat(Long id, Timestamp chatDate,
                    String content, ChatRoom chatRoom, ChatBot chatBot) {
        this.id = id;
        this.chatDate = chatDate;
        this.content = content;
        this.chatRoom = chatRoom;
        this.chatBot = chatBot;
    }
}
