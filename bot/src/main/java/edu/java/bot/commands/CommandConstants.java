package edu.java.bot.commands;

public final class CommandConstants {
    public static final String START_COMMAND = "/start";
    public static final String HELP_COMMAND = "/help";
    public static final String TRACK_COMMAND = "/track";
    public static final String UNTRACK_COMMAND = "/untrack";
    public static final String LIST_COMMAND = "/list";

    public static final String START_DESCRIPTION = "register";
    public static final String HELP_DESCRIPTION = "output of all commands description";
    public static final String TRACK_DESCRIPTION = "start track site";
    public static final String UNTRACK_DESCRIPTION = "delete site from track list";
    public static final String LIST_DESCRIPTION = "list of tracked site";

    public static final String START_REPLY = "You are registered";
    public static final String TRACK_REPLY = "What site do you want to track?";
    public static final String UNTRACK_REPLY = "What site do you want to end track?";
    public static final String EMPTY_LIST_REPLY = "You are tracking nothing";
    public static final String FILLED_LIST_REPLY = "List of tracked sites:";

    public static final String UNSUPPORTED_COMMAND = "Unsopported command!";

    private CommandConstants() {
    }
}
