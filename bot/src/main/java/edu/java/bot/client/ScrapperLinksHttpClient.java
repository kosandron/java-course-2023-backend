package edu.java.bot.client;

import edu.java.bot.client.dto.AddLinkRequest;
import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.client.dto.ListLinksResponse;
import edu.java.bot.client.dto.RemoveLinkRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperLinksHttpClient {
    private static final String BASE_URL = "http://localhost:8080";
    public static final String LINKS_PATH = "/links";
    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";
    private final WebClient webClient;

    public ScrapperLinksHttpClient() {
        this(BASE_URL);
    }

    public ScrapperLinksHttpClient(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public ListLinksResponse getLinks(Long tgChatId) {
        return webClient
            .get()
            .uri(LINKS_PATH)
            .header(TG_CHAT_HEADER, tgChatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
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
            .block();
    }
}
