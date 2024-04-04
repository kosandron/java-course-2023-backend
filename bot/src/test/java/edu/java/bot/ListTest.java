package edu.java.bot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.repositories.LinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class ListTest extends AbstractTest {
    @Autowired
    private LinkRepository repository;
    @Autowired
    private ListCommand command;

    @Test
    public void nothingListTest() {
        createMockObjects(command.command(), 2L);

        SendMessage message = command.handle(mockUpdate);

        assertThat(message.getParameters().get("text").toString()).isEqualTo("You are tracking nothing");
    }

    @Test
    public void listWithSomeLinksTest() {
        createMockObjects(command.command());
        String url1 = "https://github.com/kosandron/java-course-2023-backend";
        String url2 = "https://github.com/kosandron/java-course-2023";
        repository.trackLink(1L, url1);
        repository.trackLink(1L, url2);

        SendMessage message = command.handle(mockUpdate);

        assertThat(message.getParameters().get("text").toString()).contains(url1);
        assertThat(message.getParameters().get("text").toString()).contains(url2);
    }
}
