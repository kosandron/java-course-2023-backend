package edu.java.bot.client;

import edu.java.bot.client.dto.AddLinkRequest;
import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.client.dto.ListLinksResponse;
import edu.java.bot.client.dto.RemoveLinkRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class ScrapperLinksHttpClient {
    private static final String BASE_URL = "http://localhost:8080";
    public static final String LINKS_PATH = "/links";
    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";
    private final WebClient webClient;
    private final Retry retry;

    public ScrapperLinksHttpClient(Retry retry) {
        this(BASE_URL, retry);
    }

    public ScrapperLinksHttpClient(String baseUrl, Retry retry) {
        this.webClient = WebClient.create(baseUrl);
        this.retry = retry;
    }

    public ListLinksResponse getLinks(Long tgChatId) {
        return webClient
            .get()
            .uri(LINKS_PATH)
            .header(TG_CHAT_HEADER, tgChatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retry)
            .block();
    }

    public LinkResponse addLink(Long tgChatId, String link) {
        return webClient
            .post()
            .uri(LINKS_PATH)
            .header(TG_CHAT_HEADER, tgChatId.toString())
            .bodyValue(new AddLinkRequest((link)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();
    }

    public LinkResponse deleteLink(Long tgChatId, String link) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_PATH)
            .header(TG_CHAT_HEADER, tgChatId.toString())
            .bodyValue(new RemoveLinkRequest(link))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();
    }
}
