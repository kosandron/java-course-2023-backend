package edu.java.bot.replies.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.replies.Reply;
import java.util.List;

public interface ReplyProcessor {
    List<Reply> replies();

    SendMessage process(Update update);
}
