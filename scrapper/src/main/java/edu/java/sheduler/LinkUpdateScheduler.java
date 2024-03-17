package edu.java.sheduler;

import edu.java.client.BotHttpClient;
import edu.java.domain.model.Link;
import edu.java.services.LinkChatService;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdateScheduler {
    private final static int CHECKED_LINKS_COUNT_PER_UPDATE = 5;
    private final BotHttpClient botClient;
    private final LinkService linkService;
    private final LinkChatService linkChatService;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public int update() {
        log.info("OnUpdate!");

        List<Link> links = linkService.listByOldestCheck(CHECKED_LINKS_COUNT_PER_UPDATE);
        int updatedCount = 0;
        for (var link : links) {
            // Updating...
            linkService.updateLastChecked(link.getId(), OffsetDateTime.now());
        }

        return updatedCount;
    }
}
