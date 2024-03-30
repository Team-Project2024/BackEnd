package Hoseo.GraduationProject.Chat.Repository;

import Hoseo.GraduationProject.Chat.Domain.ChatBot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBotRepository extends JpaRepository<ChatBot, Long> {
}
