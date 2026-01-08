package com.practice.researchopsproject.exception.customException;

public class TokenExpireException extends Exception {
    public TokenExpireException(String tokenExpire) {
        super(tokenExpire);
    }
}
