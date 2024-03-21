package edu.java.controllers;

import edu.java.dto.ApiErrorResponse;
import edu.java.dto.TgChatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ScrapperApiTgChatController.TG_CHAT_MAPPING)
public class ScrapperApiTgChatController {
    public final static String TG_CHAT_MAPPING = "/tg-chat";
    public final static String ID_MAPPING = "/{id}";

    @Operation(summary = "Добавить чат", description = "Ok")
    @ApiResponse(responseCode = "200", description = "Чат успешно добавлен")
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
        responseCode = "404", description = "Чат не найден",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PostMapping(ID_MAPPING)
    public TgChatResponse addChat(@PathVariable("id") @Min(0) long tgChatId) {
        return new TgChatResponse();
    }

    @Operation(summary = "Убрать чат", description = "Ok")
    @ApiResponse(responseCode = "200", description = "Чат успешно убран")
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
        responseCode = "404", description = "Чат не найден",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @DeleteMapping(ID_MAPPING)
    public TgChatResponse deleteChat(@PathVariable("id") @Min(0) long tgChatId) {
        return new TgChatResponse();
    }
}
