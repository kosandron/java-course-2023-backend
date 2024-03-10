package edu.java.bot;
/*
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.bot.LinkTrackerBot;
import edu.java.bot.commands.processors.DefaultCommandProcessor;
import edu.java.bot.replies.processors.DefaultReplyProcessor;
import edu.java.bot.repositories.LinkRepository;
import java.util.List;
import edu.java.bot.repositories.LinkValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BotCommandsTests {
    private final long chatId = 1L;
    private final Update testUpdate = Mockito.mock(Update.class);
    private LinkTrackerBot linkTrackerBot;
    private LinkRepository linkRepository;

    @Captor
    private ArgumentCaptor<? extends BaseRequest<SendMessage, SendResponse>> messageCaptor;
    @Mock
    private TelegramBot bot;


    @BeforeEach
    public void initResources() {
        linkRepository = Mockito.mock(LinkRepository.class);
        var messageProcessor = new DefaultCommandProcessor();
        var replyProcessor = new DefaultReplyProcessor();
        linkTrackerBot = new LinkTrackerBot(bot, replyProcessor, messageProcessor);
    }

    private void setUpdateMessage(String text) {
        var message = Mockito.mock(Message.class);
        var chat = Mockito.mock(Chat.class);
        when(testUpdate.message()).thenReturn(message);
        when(testUpdate.message().chat()).thenReturn(chat);
        when(testUpdate.message().chat().id()).thenReturn(chatId);
        when(testUpdate.message().text()).thenReturn(text);
    }

    private void setReplyMessage(String replyOnText) {
        var message = Mockito.mock(Message.class);
        when(message.text()).thenReturn(replyOnText);
        when(testUpdate.message().replyToMessage()).thenReturn(message);
    }

    private String executeCommand(String messageText, boolean isReply, String replyOnText) {
        setUpdateMessage(messageText);
        if (isReply) {
            setReplyMessage(replyOnText);
        }

        linkTrackerBot.process(List.of(testUpdate));

        Mockito.verify(bot).execute(messageCaptor.capture());

        BaseRequest<SendMessage, SendResponse> value = messageCaptor.getValue();
        return (String) value.getParameters().get("text");
    }

    @Test
    public void unknownCommandTest() {
        // Arrange
        String unknownCommand = "/unknownCommand";

        // Act
        String answer = executeCommand(unknownCommand, false, null);

        // Assert
        assertThat(testUpdate.message().text()).isEqualTo(unknownCommand);
        assertThat(answer).isEqualTo("Unsopported command!");
    }

    @Test
    public void emptyListTrackedTest() {
        // Arrange
        when(linkRepository.getTrackedLinks(chatId)).thenReturn(List.of());
        String command = "/list";

        // Act
        String answer = executeCommand(command, false, null);

        // Assert
        assertThat(testUpdate.message().text()).isEqualTo(command);
        assertThat(answer).isEqualTo("You are tracking nothing");
    }
/*
    @Test
    public void listCommandTest() {
        // Arrange
        String link1 = "https://github.com";
        String link2 = "https://stackoverflow.com";
        when(linkRepository.getTrackedLinks(chatId)).thenReturn(List.of(link1, link2));

        String command = CommandConstants.LIST_COMMAND;

        // Act
        String answer = executeCommand(command, false, null);

        // Assert
        Mockito.verify(linkRepository).getTrackedLinks(chatId);
        assertThat(testUpdate.message().text()).isEqualTo(command);
        assertThat(answer).startsWith(CommandConstants.FILLED_LIST_REPLY);
        assertThat(answer).contains(link1).contains(link2);
    }

    @Test
    public void helpCommandTest() {
        // Arrange
        String command = CommandConstants.HELP_COMMAND;

        // Act
        String answer = executeCommand(command, false, null);

        // Assert
        assertThat(testUpdate.message().text()).isEqualTo(command);
        assertThat(answer)
            .contains(CommandConstants.START_COMMAND).contains(CommandConstants.START_DESCRIPTION)
            .contains(CommandConstants.HELP_COMMAND).contains(CommandConstants.HELP_DESCRIPTION)
            .contains(CommandConstants.LIST_COMMAND).contains(CommandConstants.LIST_DESCRIPTION)
            .contains(CommandConstants.TRACK_COMMAND).contains(CommandConstants.TRACK_DESCRIPTION)
            .contains(CommandConstants.UNTRACK_COMMAND).contains(CommandConstants.UNTRACK_DESCRIPTION);
    }

    @Test
    public void trackCommandTest() {
        // Arrange
        String command = CommandConstants.TRACK_COMMAND;

        // Act
        String answer = executeCommand(command, false, null);

        // Assert
        assertThat(testUpdate.message().text()).isEqualTo(command);
        assertThat(answer).isEqualTo(CommandConstants.TRACK_REPLY);
    }

    @Test
    public void untrackCommandTest() {
        // Arrange
        String command = CommandConstants.UNTRACK_COMMAND;

        // Act
        String answer = executeCommand(command, false, null);

        // Assert
        assertThat(testUpdate.message().text()).isEqualTo(command);
        assertThat(answer).isEqualTo(CommandConstants.UNTRACK_REPLY);
    }

    @Test
    public void replySuccessTrackTest() {
        // Arrange
        String link = "https://github.com";
        String replyOnText = CommandConstants.TRACK_REPLY;
        when(LinkValidator.isValidLink(link)).thenReturn(true);

        // Act
        String answer = executeCommand(link, true, replyOnText);

        // Assert
        Mockito.verify(linkRepository).trackLink(chatId, link);
        assertThat(testUpdate.message().text()).isEqualTo(link);
        assertThat(answer).isEqualTo(ReplyConstants.TRACK_SUCCESS);
    }

    @Test
    public void replySuccessUntrackTest() {
        // Arrange
        String link = "https://github.com";
        String replyOnText = CommandConstants.UNTRACK_REPLY;
        when(LinkValidator.isValidLink(link)).thenReturn(true);

        // Act
        String answer = executeCommand(link, true, replyOnText);

        // Assert
        Mockito.verify(linkRepository).untrackLink(chatId, link);
        assertThat(testUpdate.message().text()).isEqualTo(link);
        assertThat(answer).isEqualTo(ReplyConstants.UNTRACK_SUCCESS);
    }
}


*/
