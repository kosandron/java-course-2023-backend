package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.processors.CommandProcessor;
import edu.java.bot.replies.processors.ReplyProcessor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LinkTrackerBot implements Bot {
    private final TelegramBot bot;
    private final ReplyProcessor replyProcessor;
    private final CommandProcessor commandProcessor;

    @Autowired
    public LinkTrackerBot(
        @Value("${app.telegram-token}") String botToken,
        @Qualifier("DefaultReplyProcessor") ReplyProcessor replyProcessor,
        @Qualifier("DefaultCommandProcessor") CommandProcessor commandProcessor
    ) {
        this.bot = new TelegramBot(botToken);
        this.replyProcessor = replyProcessor;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            var request = isReply(update)
                ? replyProcessor.process(update)
                : commandProcessor.process(update);
            execute(request);
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        setupMenu();
        bot.setUpdatesListener(this);
    }

    @Override
    public void close() {
        bot.removeGetUpdatesListener();
    }

    private void setupMenu() {
        BotCommand[] botCommands = commandProcessor.commands()
            .stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new);

        bot.execute(new SetMyCommands(botCommands));
    }

    private boolean isReply(Update update) {
        return update.message().replyToMessage() != null;
    }
}
