package edu.java.bot.commands.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandConstants;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.repositories.LinkRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("DefaultCommandProcessor")
public class DefaultCommandProcessor implements CommandProcessor {
    private final List<? extends Command> commands;

    @Autowired
    public DefaultCommandProcessor(LinkRepository linkRepository) {
        var emptyHelpCommand = new HelpCommand();
        var tempCommandsList = new ArrayList<>(
            List.of(
                emptyHelpCommand,
                new StartCommand(),
                new TrackCommand(),
                new UntrackCommand(),
                new ListCommand(linkRepository)
            )
        );
        tempCommandsList.add(new HelpCommand(tempCommandsList.toArray(Command[]::new)));
        tempCommandsList.remove(emptyHelpCommand);
        commands = tempCommandsList;
    }

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        for (var command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }

        return new SendMessage(update.message().chat().id(), CommandConstants.UNSUPPORTED_COMMAND);
    }
}
