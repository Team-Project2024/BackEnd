package Hoseo.GraduationProject.Chat.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_bot")
@NoArgsConstructor
public class ChatBot {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_chat_id", unique = true)
    private UserChat userChat;

    @Builder
    public ChatBot(Long id, String content, UserChat userChat) {
        this.id = id;
        this.content = content;
        this.userChat = userChat;
    }
}