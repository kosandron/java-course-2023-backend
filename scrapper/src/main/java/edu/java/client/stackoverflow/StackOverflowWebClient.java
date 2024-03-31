package edu.java.client.stackoverflow;

import edu.java.client.dto.StackOverFlowResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class StackOverflowWebClient implements StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com/2.3/";
    private final WebClient webClient;
    private final Retry retry;

    public StackOverflowWebClient(Retry retry) {
        this(BASE_URL, retry);
    }

    public StackOverflowWebClient(String url, Retry retry) {
        this.webClient = WebClient.create(url);
        this.retry = retry;
    }

    @Override
    public StackOverFlowResponse fetchQuestion(@NotNull Long questionId) {
        return this.webClient
            .get()
            .uri("/questions/{questionId}", questionId)
            .retrieve()
            .bodyToMono(StackOverFlowResponse.class)
            .retryWhen(retry)
            .block();
    }
}
