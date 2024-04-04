package edu.java.bot.client.dto;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
