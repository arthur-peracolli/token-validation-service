package com.challenge.token_validation_service.domain.models;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Claims {

    private final String name;
    private final String role;
    private final String seed;

    public boolean isComplete() {
        return name != null && !name.isEmpty()
                && role != null && !role.isEmpty()
                && seed != null && !seed.isEmpty();
    }

    public long countFilledClaims() {
        long count = 0;
        if (name != null && !name.isEmpty()) count++;
        if (role != null && !role.isEmpty()) count++;
        if (seed != null && !seed.isEmpty()) count++;
        return count;
    }
}