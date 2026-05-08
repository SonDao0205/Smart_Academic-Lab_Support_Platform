package com.dgnl.smartacademyandlabsupportplatform.exception;

import lombok.Getter;

@Getter
public class MissingInput extends RuntimeException {
    private final String field;
    public MissingInput(String message, String field) {
        super(message);
        this.field = field;
    }
}
