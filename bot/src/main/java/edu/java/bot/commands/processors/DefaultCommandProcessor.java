package edu.java.bot.commands.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("DefaultCommandProcessor")
public class DefaultCommandProcessor implements CommandProcessor {
    private static final String UNSUPPORTED_COMMAND = "Unsopported command!";
    private final List<Command> commands;
    private final Counter counter;

    public DefaultCommandProcessor(List<Command> commands, MeterRegistry meterRegistry) {
        this.commands = commands;
        this.counter = meterRegistry.counter("message_processed_count");
    }

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        counter.increment();
        for (var command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }

        return new SendMessage(update.message().chat().id(), UNSUPPORTED_COMMAND);
    }
}
