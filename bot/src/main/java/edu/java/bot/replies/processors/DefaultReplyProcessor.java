package edu.java.bot.replies.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.replies.Reply;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("DefaultReplyProcessor")
public class DefaultReplyProcessor implements ReplyProcessor {
    private static final String UNSUPPORTED_COMMAND = "Unsopported command!";
    private final List<Reply> replies;

    public DefaultReplyProcessor(List<Reply> replies) {
        this.replies = replies;
    }

    @Override
    public List<Reply> replies() {
        return replies;
    }

    @Override
    public SendMessage process(Update update) {
        for (var reply : replies) {
            if (reply.supports(update)) {
                return reply.handle(update);
            }
        }

        return new SendMessage(update.message().chat().id(), UNSUPPORTED_COMMAND);
    }
}
