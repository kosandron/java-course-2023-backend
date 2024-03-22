package edu.java.bot.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("DefaultLinkRepository")
public class DefaultLinkRepository implements LinkRepository {
    private final Map<Long, List<String>> linkListByTelegramId = new HashMap<>();

    @Override
    public List<String> getTrackedLinks(long telegramId) {
        return linkListByTelegramId.getOrDefault(telegramId, List.of());
    }

    @Override
    public boolean trackLink(long telegramId, String link) {
        if (!linkListByTelegramId.containsKey(telegramId)) {
            linkListByTelegramId.put(telegramId, new ArrayList<>());
        }
        var list = linkListByTelegramId.get(telegramId);
        if (list.contains(link)) {
            return true;
        }
        return list.add(link);
    }

    @Override
    public boolean untrackLink(long telegramId, String link) {
        var list = linkListByTelegramId.get(telegramId);
        if (list != null) {
            return list.remove(link);
        }
        return false;
    }
}
