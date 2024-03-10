package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public abstract class AbstractTest {
    protected final long chatId = 1L;
    @MockBean
    protected Update mockUpdate;
    @Mock
    protected TelegramBot bot;
    @Captor
    protected ArgumentCaptor<? extends BaseRequest<SendMessage, SendResponse>> messageCaptor;

    protected void createMockObjects(String commandReply) {
        var message = Mockito.mock(Message.class);
        var chat = Mockito.mock(Chat.class);
        when(mockUpdate.message()).thenReturn(message);
        when(mockUpdate.message().chat()).thenReturn(chat);
        when(mockUpdate.message().chat().id()).thenReturn(chatId);
        when(mockUpdate.message().text()).thenReturn(commandReply);
    }
}
