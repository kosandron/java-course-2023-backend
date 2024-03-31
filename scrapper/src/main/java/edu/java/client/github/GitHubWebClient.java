package edu.java.client.github;

import edu.java.client.dto.GithubResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class GitHubWebClient implements GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;
    private final Retry retry;

    public GitHubWebClient(Retry retry) {
        this(BASE_URL, retry);
    }

    public GitHubWebClient(String url, Retry retry) {
        this.webClient = WebClient.create(url);
        this.retry = retry;
    }

    @Override
    public GithubResponse fetchRepository(@NotNull String owner, @NotNull String repository) {
        return this.webClient
            .get()
            .uri("/repos/{owner}/{repository}", owner, repository)
            .retrieve()
            .bodyToMono(GithubResponse.class)
            .retryWhen(retry)
            .block();
    }
}
