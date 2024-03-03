package edu.java.bot.client;

import edu.java.bot.client.dto.TgChatResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperTgChatHttpClient {
    private static final String BASE_URL = "http:///localhost:8080";
    private static final String TG_CHAT_BY_ID_LINK = "/tg-chat/{id}";
    private final WebClient webClient;

    public ScrapperTgChatHttpClient() {
        this(BASE_URL);
    }

    public ScrapperTgChatHttpClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public TgChatResponse addTgChat(Long tgChatId) {
        return webClient
            .post()
            .uri(TG_CHAT_BY_ID_LINK, tgChatId)
            .retrieve()
            .bodyToMono(TgChatResponse.class)
            .block();
    }

    public TgChatResponse deleteTgChat(Long tgChatId) {
        return webClient
            .delete()
            .uri(TG_CHAT_BY_ID_LINK, tgChatId)
            .retrieve()
            .bodyToMono(TgChatResponse.class)
            .block();
    }
}
