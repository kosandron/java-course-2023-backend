package edu.java.dto.database;

import edu.java.domain.jdbc.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatDto {
    private Long id;

    public static ChatDto fromJdbcChat(Chat chat) {
        return new ChatDto(chat.getChatId());
    }

    public static ChatDto fromJpaChat(edu.java.domain.jpa.model.Chat chat) {
        return new ChatDto(chat.getId());
    }
}
