package edu.java.bot.commands.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("DefaultCommandProcessor")
@RequiredArgsConstructor
public class DefaultCommandProcessor implements CommandProcessor {
    private static final String UNSUPPORTED_COMMAND = "Unsopported command!";
    private final List<Command> commands;

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        for (var command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }

        return new SendMessage(update.message().chat().id(), UNSUPPORTED_COMMAND);
    }
}
