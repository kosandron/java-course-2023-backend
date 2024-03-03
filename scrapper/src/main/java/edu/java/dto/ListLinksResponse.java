package edu.java.dto;

public record ListLinksResponse(
    LinkResponse[] links,
    Integer size
) {
}
