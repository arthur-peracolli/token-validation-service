package com.challenge.token_validation_service.shared.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PrimeNumberValidator {

    private PrimeNumberValidator() {
    }

    public static boolean isPrime(int number) {
        if (number <= 1) {
            log.debug("Número {} é menor ou igual a 1, não é primo", number);
            return false;
        }

        if (number == 2) {
            return true;
        }

        if (number % 2 == 0) {
            log.debug("Número {} é par, não é primo", number);
            return false;
        }

        int limit = (int) Math.sqrt(number);
        for (int i = 3; i <= limit; i += 2) {
            if (number % i == 0) {
                log.debug("Número {} é divisível por {}, não é primo", number, i);
                return false;
            }
        }

        return true;
    }

    public static boolean isPrime(String numberStr) {
        try {
            int number = Integer.parseInt(numberStr);
            return isPrime(number);
        } catch (NumberFormatException e) {
            log.warn("Seed não é um número válido: {}", numberStr);
            return false;
        }
    }
}