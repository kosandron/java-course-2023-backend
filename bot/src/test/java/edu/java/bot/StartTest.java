package edu.java.bot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.StartCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class StartTest extends AbstractTest {
    @Autowired
    private StartCommand command;

    @Test
    public void startTest() {
        createMockObjects(command.command());

        SendMessage message = command.handle(mockUpdate);

        assertThat(message.getParameters().get("text").toString()).isEqualTo("You are registered");
    }
}
