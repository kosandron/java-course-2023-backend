package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public abstract class AbstractTest {
    protected final long chatId = 1L;
    @MockBean
    protected Update mockUpdate;

    protected void createMockObjects(String commandReply) {
        var message = Mockito.mock(Message.class);
        var chat = Mockito.mock(Chat.class);
        when(mockUpdate.message()).thenReturn(message);
        when(mockUpdate.message().chat()).thenReturn(chat);
        when(mockUpdate.message().chat().id()).thenReturn(chatId);
        when(mockUpdate.message().text()).thenReturn(commandReply);
    }

    protected void createMockObjects(String commandReply, long chatId) {
        var message = Mockito.mock(Message.class);
        var chat = Mockito.mock(Chat.class);
        when(mockUpdate.message()).thenReturn(message);
        when(mockUpdate.message().chat()).thenReturn(chat);
        when(mockUpdate.message().chat().id()).thenReturn(chatId);
        when(mockUpdate.message().text()).thenReturn(commandReply);
    }

    protected void setReplyText(String replyText) {
        var message = Mockito.mock(Message.class);
        when(message.text()).thenReturn(replyText);
        when(mockUpdate.message().replyToMessage()).thenReturn(message);
    }
}
