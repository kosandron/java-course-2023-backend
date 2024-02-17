package edu.java.bot.commands.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;

public interface CommandProcessor {
    List<? extends Command> commands();

    SendMessage process(Update update);
}
