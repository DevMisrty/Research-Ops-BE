package com.practice.researchopsproject.exception.customException;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String caseNotFound) {
        super(caseNotFound);
    }
}
