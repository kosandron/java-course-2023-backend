package edu.java.bot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.replies.UntrackReply;
import edu.java.bot.repositories.LinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class UntrackTest extends AbstractTest {
    @Autowired
    private UntrackCommand command;
    @Autowired
    private UntrackReply reply;
    @Autowired
    private LinkRepository repository;

    @Test
    public void untrackTest() {
        createMockObjects(command.command());

        SendMessage message = command.handle(mockUpdate);

        assertThat(message.getParameters().get("text").toString()).isEqualTo("What site do you want to end track?");
    }

    @Test
    public void successUntrackReplyTest() {
        String url1 = "https://github.com/kosandron/java-course-2023";
        String url2 = "https://github.com/kosandron/java-course-2023-backend";
        System.out.println(repository.getTrackedLinks(1L).toString());
        repository.trackLink(1L, url1);
        repository.trackLink(1L, url2);
        createMockObjects(url1);
        setReplyText("What site do you want to end track?");

        SendMessage message = reply.handle(mockUpdate);

        assertThat(message.getParameters().get("text").toString()).isEqualTo("Tracking of this resource ended!");
        assertThat(repository.getTrackedLinks(1L)).doesNotContain(url1);
        assertThat(repository.getTrackedLinks(1L)).contains(url2);
    }

    @Test
    public void badUntrackReplyTest() {
        String url1 = "https://github.com/kosandron/java-course-2023";
        String url2 = "https://github.com/kosandron/java-course-2023-backend";
        String notURL = "blablabla";
        repository.trackLink(1L, url1);
        repository.trackLink(1L, url2);
        createMockObjects(notURL);
        setReplyText("What site do you want to end track?");

        SendMessage message = reply.handle(mockUpdate);

        assertThat(message.getParameters().get("text").toString()).isEqualTo("Cannot to end to track this resource!");
        assertThat(repository.getTrackedLinks(1L)).contains(url1);
        assertThat(repository.getTrackedLinks(1L)).contains(url2);
    }
}
