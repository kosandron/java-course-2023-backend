package edu.java.controllers;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import jakarta.validation.constraints.Min;
import java.net.URI;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class ScrapperApiLinksController {
    private final static String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    @GetMapping
    public ListLinksResponse getTrackedLinks(
        @RequestHeader(TG_CHAT_ID_HEADER) @Min(0) long tgChatId
    ) {
        LinkResponse[] array = {};
        return new ListLinksResponse(array, array.length);
    }

    @PostMapping
    public LinkResponse addLink(
        @RequestHeader(TG_CHAT_ID_HEADER) @Min(0) long tgChatId,
        @Validated @RequestBody AddLinkRequest request
    ) {
        return new LinkResponse(tgChatId, URI.create(request.link()));
    }

    @DeleteMapping
    public LinkResponse removeLink(
        @RequestHeader(TG_CHAT_ID_HEADER) @Min(0) long tgChatId,
        @Validated @RequestBody RemoveLinkRequest request
    ) {
        return new LinkResponse(tgChatId, URI.create(request.link()));
    }
}
