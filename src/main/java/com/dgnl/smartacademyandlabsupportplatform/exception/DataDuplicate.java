package com.dgnl.smartacademyandlabsupportplatform.exception;


import lombok.Getter;

@Getter
public class DataDuplicate extends RuntimeException {
    private final String field;
    public DataDuplicate(String field, String message) {
        super(message);
        this.field = field;
    }
}
