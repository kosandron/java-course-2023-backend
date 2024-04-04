package edu.java.bot.client.dto;

public record ListLinksResponse(
    LinkResponse[] links,
    Integer size
) {
}
