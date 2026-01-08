package com.practice.researchopsproject.exception.customException;

public class CaseNotFoundException extends Exception {
    public CaseNotFoundException(String caseNotFound) {
        super(caseNotFound);
    }
}
