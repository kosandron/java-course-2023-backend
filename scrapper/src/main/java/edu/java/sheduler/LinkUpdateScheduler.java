package edu.java.sheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdateScheduler implements LinkUpdater {
    private final static int CHECKED_LINKS_COUNT_PER_UPDATE = 5;
    private final BotHttpClient botClient;
    private final LinkService linkService;
    private final LinkChatService linkChatService;
    private final MainChecker updateChecker;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        List<Link> links = linkService.listByOldestCheck(CHECKED_LINKS_COUNT_PER_UPDATE);
        for (var link : links) {
            UpdateCheckResult result = updateChecker.check(link);
            if (result.hasUpdate()) {

                botClient.update(
                    link.getId(),
                    URI.create(link.getUrl()),
                    result.description(),
                    linkChatService.listAllChatsByLinkId(link.getId()).toArray(Long[]::new)
                );

                linkService.updateLastModified(link.getId(), link.getLastModified());
            }
            linkService.updateLastChecked(link.getId(), OffsetDateTime.now());
        }
    }
}
