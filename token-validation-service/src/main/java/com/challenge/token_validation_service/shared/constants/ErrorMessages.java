package com.challenge.token_validation_service.shared.constants;

public final class ErrorMessages {

    private ErrorMessages() {
    }

    public static final String CLAIMS_NULL = "Claims estão nulas";
    public static final String CLAIM_COUNT_INVALID = "Número de claims inválido. Esperado: %d, encontrado: %d";

    public static final String NAME_EMPTY = "Name está vazio";
    public static final String NAME_TOO_LONG = "Name excede 256 caracteres";
    public static final String NAME_CONTAINS_NUMBER = "Name contém caracteres numéricos";

    public static final String ROLE_EMPTY = "Role está vazia";
    public static final String ROLE_INVALID = "Role inválida: %s. Valores permitidos: %s";

    public static final String SEED_EMPTY = "Seed está vazia";
    public static final String SEED_NOT_PRIME = "Seed não é um número primo";
    public static final String SEED_INVALID_NUMBER = "Seed não é um número válido";
}
