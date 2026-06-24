package com.challenge.token_validation_service.application.controllers;

import com.challenge.token_validation_service.application.dtos.ValidationResponse;
import com.challenge.token_validation_service.domain.services.TokenValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Validação de JWT", description = "Endpoints para validação de tokens JWT")
public class ValidationController {

  private final TokenValidationService tokenValidationService;

  @Operation(
      summary = "Validar um token JWT",
      description =
          "Recebe um token JWT e verifica se ele é válido conforme as regras de negócio: "
              + "exatamente 3 claims (Name, Role, Seed), "
              + "Name sem números, "
              + "Role em {Admin, Member, External}, "
              + "Seed como número primo.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Validação concluída com sucesso",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ValidationResponse.class),
                    examples = @ExampleObject(value = "{\"valid\": true}"))),
        @ApiResponse(
            responseCode = "400",
            description = "Parâmetro 'token' ausente ou inválido",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(value = "{\"error\": \"Parâmetro 'token' é obrigatório\"}")))
      })
  @GetMapping("/validate")
  public ResponseEntity<ValidationResponse> validateToken(
      @Parameter(
              description = "Token JWT a ser validado",
              required = true,
              example =
                  "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg")
          @RequestParam
          String token) {
    log.info(
        "Token validation request received. tokenLength={}, requestId={}",
        token.length(),
        MDC.get("requestId"));

    boolean isValid = tokenValidationService.validateToken(token);

    log.info("Token validation completed. valid={}, requestId={}", isValid, MDC.get("requestId"));

    return ResponseEntity.ok(new ValidationResponse(isValid));
  }
}
