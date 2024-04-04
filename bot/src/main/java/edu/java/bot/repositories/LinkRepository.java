package edu.java.bot.repositories;

import java.util.List;

public interface LinkRepository {

    List<String> getTrackedLinks(long telegramId);

    boolean trackLink(long telegramId, String link);

    boolean untrackLink(long telegramId, String link);
}
