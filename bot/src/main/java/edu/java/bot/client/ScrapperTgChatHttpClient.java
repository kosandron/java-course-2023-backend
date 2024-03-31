package edu.java.bot.client;

import edu.java.bot.client.dto.TgChatResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class ScrapperTgChatHttpClient {
    private static final String BASE_URL = "http://localhost:8080";
    public static final String TG_CHAT_BY_ID_LINK = "/tg-chat/{id}";
    private final WebClient webClient;
    private final Retry retry;

    public ScrapperTgChatHttpClient(Retry retry) {
        this(BASE_URL, retry);
    }

    public ScrapperTgChatHttpClient(String baseUrl, Retry retry) {
        this.webClient = WebClient.create(baseUrl);
        this.retry = retry;
    }

    public TgChatResponse addTgChat(Long tgChatId) {
        return webClient
            .post()
            .uri(TG_CHAT_BY_ID_LINK, tgChatId)
            .retrieve()
            .bodyToMono(TgChatResponse.class)
            .retryWhen(retry)
            .block();
    }

    public TgChatResponse deleteTgChat(Long tgChatId) {
        return webClient
            .delete()
            .uri(TG_CHAT_BY_ID_LINK, tgChatId)
            .retrieve()
            .bodyToMono(TgChatResponse.class)
            .retryWhen(retry)
            .block();
    }
}
