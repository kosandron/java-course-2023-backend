package edu.java.bot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.replies.TrackReply;
import edu.java.bot.repositories.LinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class TrackTest extends AbstractTest {
    @Autowired
    private TrackCommand command;
    @Autowired
    private TrackReply reply;
    @Autowired
    private LinkRepository repository;

    @Test
    public void trackTest() {
        createMockObjects(command.command());

        SendMessage message = command.handle(mockUpdate);

        assertThat(message.getParameters().get("text").toString()).isEqualTo("What site do you want to track?");
    }

    @Test
    public void successTrackReplyTest() {
        String url = "https://github.com/kosandron/java-course-2023";
        createMockObjects(url);
        setReplyText("What site do you want to track?");

        SendMessage message = reply.handle(mockUpdate);

        assertThat(message.getParameters().get("text").toString()).isEqualTo("Success! This resource is tracking!");
        assertThat(repository.getTrackedLinks(1L)).contains(url);
    }

    @Test
    public void badTrackReplyTest() {
        String url = "blablabla";
        createMockObjects(url);
        setReplyText("What site do you want to track?");

        SendMessage message = reply.handle(mockUpdate);

        assertThat(message.getParameters().get("text").toString()).isEqualTo("Cannot to begin track this resource!");
    }
}
