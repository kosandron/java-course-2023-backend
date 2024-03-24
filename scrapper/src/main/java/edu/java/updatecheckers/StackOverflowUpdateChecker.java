package edu.java.updatecheckers;

import edu.java.client.dto.StackOverFlowResponse;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.domain.models.Link;
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
    public UpdateCheckResult check(Link link) {
        if (supports(link.getUrl())) {
            List<String> extensions = PathParser.getPathComponents(link.getUrl());
            if (extensions.size() < MIN_WORDS_COUNT) {
                return UpdateCheckResult.getDefault();
            }

            StackOverFlowResponse response = stackOverflowClient.fetchQuestion(Long.valueOf(extensions.get(
                QUESTION_ID_POSITION)));
            if (link.getLastModified().isBefore(response.lastModified())) {
                link.setLastModified(response.lastModified());
                return new UpdateCheckResult(true, response.title());
            }
        }

        return UpdateCheckResult.getDefault();
    }

    @Override
    public boolean supports(String url) {
        return url.contains(STACKOVERFLOW_DOMAIN);
    }
}
