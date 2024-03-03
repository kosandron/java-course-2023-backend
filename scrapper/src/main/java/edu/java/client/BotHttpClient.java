package edu.java.client;

import edu.java.client.dto.LinkUpdateRequest;
import edu.java.client.dto.LinkUpdateResponse;
import java.net.URI;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotHttpClient {
    private static final String BASE_URL = "http:///localhost:8090";
    private static final String UPDATE_PATH = "/updates";
    private final WebClient webClient;

    public BotHttpClient() {
        this(BASE_URL);
    }

    public BotHttpClient(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public LinkUpdateResponse update(long linkId, URI url, String description, Long[] tgChatIds) {
        return webClient
            .post()
            .uri(UPDATE_PATH)
            .body(
                Mono.just(new LinkUpdateRequest(linkId, url, description, tgChatIds)),
                LinkUpdateRequest.class
            )
            .retrieve()
            .bodyToMono(LinkUpdateResponse.class)
            .block();
    }
}
