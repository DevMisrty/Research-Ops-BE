package com.practice.researchopsproject.exception.customException;

public class InvaliTokenException extends Exception {
    public InvaliTokenException(String invalidToken) {
        super(invalidToken);
    }
}
