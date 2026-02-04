package com.practice.researchopsproject.exception.customException;

public class TokenNotFoundException extends Exception {
    public TokenNotFoundException(String invalidToken) {
        super(invalidToken);
    }
}
