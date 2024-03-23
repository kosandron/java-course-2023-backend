package edu.java.bot;

import edu.java.bot.dto.ApiErrorResponse;
import edu.java.bot.dto.LinkUpdateRequest;
import edu.java.bot.dto.LinkUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BotApiController.UPDATE_MAPPING)
public class BotApiController {
    public static final String UPDATE_MAPPING = "/updates";

    @Operation(summary = "Отправить обновление", description = "Ok")
    @ApiResponse(responseCode = "200", description = "Обновление обработано")
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
        responseCode = "404", description = "Чат с заданным id не найден",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PostMapping
    public LinkUpdateResponse updateLinks(@RequestBody LinkUpdateRequest request) {
        return new LinkUpdateResponse();
    }
}
