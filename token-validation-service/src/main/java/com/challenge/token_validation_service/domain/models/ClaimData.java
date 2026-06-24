package com.challenge.token_validation_service.domain.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClaimData {
    private final String name;
    private final String role;
    private final String seed;
    private final int claimCount;
}