package com.practice.springsecuritycomplete.exception.customException;

public class NoSuchUserFound extends Exception {
    public NoSuchUserFound(String userNotFound) {
        super(userNotFound);
    }
}
