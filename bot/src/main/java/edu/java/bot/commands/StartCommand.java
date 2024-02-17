package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class StartCommand implements Command {
    @Override
    public String command() {
        return CommandConstants.START_COMMAND;
    }

    @Override
    public String description() {
        return CommandConstants.START_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), CommandConstants.START_REPLY);
    }
}
