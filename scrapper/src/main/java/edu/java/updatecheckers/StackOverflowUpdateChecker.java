package edu.java.updatecheckers;

import edu.java.client.dto.StackOverflowItem;
import edu.java.client.dto.StackOverflowResponse;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.dto.database.LinkDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StackOverflowUpdateChecker implements UpdateChecker {
    private static final int MIN_WORDS_COUNT = 4;
    private static final int QUESTION_ID_POSITION = 3;
    private final StackOverflowClient stackOverflowClient;
    private static final String STACKOVERFLOW_DOMAIN = "stackoverflow.com";

    @Override
    public UpdateCheckResult check(LinkDto link) {
        if (supports(link.getUrl())) {
            List<String> extensions = PathParser.getPathComponents(link.getUrl());
            if (extensions.size() < MIN_WORDS_COUNT) {
                return UpdateCheckResult.getDefault();
            }

            StackOverflowResponse response = stackOverflowClient.fetchQuestion(Long.valueOf(extensions.get(
                QUESTION_ID_POSITION)));
            StackOverflowItem responseItem = response.items()[0];
            if (link.getLastModified().isBefore(responseItem.lastModified())) {
                link.setLastModified(responseItem.lastModified());
                return new UpdateCheckResult(true, responseItem.title());
            }
        }

        return UpdateCheckResult.getDefault();
    }

    @Override
    public boolean supports(String url) {
        return url.contains(STACKOVERFLOW_DOMAIN);
    }
}
