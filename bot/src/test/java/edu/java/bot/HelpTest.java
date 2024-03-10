package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
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
    private LinkTrackerBot linkTrackerBot;
    @Autowired
    DefaultCommandProcessor messageProcessor;
    @Autowired
    DefaultReplyProcessor replyProcessor;
    private static final  String answer = """
/list - list of tracked site
/start - register
/track - start track site
/untrack - delete site from track list
/help - output of all commands description""";
/*
    @Autowired
    public HelpTest(DefaultCommandProcessor messageProcessor, DefaultReplyProcessor replyProcessor) {
        linkTrackerBot = new LinkTrackerBot(bot, replyProcessor, messageProcessor);
    }*/

    @Test
    public void HelpCommandTest() {
        linkTrackerBot = new LinkTrackerBot(bot, replyProcessor, messageProcessor);
        createMockObjects(command.command());
        linkTrackerBot.process(List.of(mockUpdate));
        Mockito.verify(bot).execute(messageCaptor.capture());
        assertThat(answer).isEqualTo(messageCaptor.getValue().getParameters().get("text").toString());
    }
}
