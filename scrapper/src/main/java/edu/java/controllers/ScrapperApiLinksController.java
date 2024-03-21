package edu.java.controllers;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.ApiErrorResponse;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping(ScrapperApiLinksController.LINKS_MAPPING)
public class ScrapperApiLinksController {
    public final static String LINKS_MAPPING = "/links";
    private final static String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    @Operation(summary = "Получить список ссылок", description = "Ok")
    @ApiResponse(responseCode = "200", description = "Ссылки успешно получены")
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
        responseCode = "404", description = "Чат не найден",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @GetMapping
    public ListLinksResponse getTrackedLinks(
        @RequestHeader(TG_CHAT_ID_HEADER) @Min(0) long tgChatId
    ) {
        LinkResponse[] array = {};
        return new ListLinksResponse(array, array.length);
    }

    @Operation(summary = "Добавить ссылку в список", description = "Ok")
    @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена")
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
        responseCode = "404", description = "Чат не найден",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PostMapping
    public LinkResponse addLink(
        @RequestHeader(TG_CHAT_ID_HEADER) @Min(0) long tgChatId,
        @Validated @RequestBody AddLinkRequest request
    ) {
        return new LinkResponse(tgChatId, URI.create(request.link()));
    }

    @Operation(summary = "Удалить ссылку из списка", description = "Ok")
    @ApiResponse(responseCode = "200", description = "Ссылка успешно удалена")
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
        responseCode = "404", description = "Чат не найден",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @DeleteMapping
    public LinkResponse removeLink(
        @RequestHeader(TG_CHAT_ID_HEADER) @Min(0) long tgChatId,
        @Validated @RequestBody RemoveLinkRequest request
    ) {
        return new LinkResponse(tgChatId, URI.create(request.link()));
    }
}
