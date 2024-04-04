package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.LinkTrackerBot;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.processors.DefaultCommandProcessor;
import edu.java.bot.replies.processors.DefaultReplyProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class HelpTest extends AbstractTest {
    @Autowired
    private HelpCommand command;
    private static final  String answer = """
/list - list of tracked site
/start - register
/track - start track site
/untrack - delete site from track list
/help - output of all commands description""";

    @Test
    public void HelpCommandTest() {
        createMockObjects(command.command());

        SendMessage message = command.handle(mockUpdate);

        assertThat(answer).isEqualTo(message.getParameters().get("text").toString());
    }
}
