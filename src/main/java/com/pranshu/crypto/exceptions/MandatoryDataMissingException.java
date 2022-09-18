package com.pranshu.crypto.exceptions;

import lombok.Getter;
import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;

@Getter
public class MandatoryDataMissingException extends RuntimeException {

    private HttpStatus httpStatus;

    public MandatoryDataMissingException(String message) {
        super(message);
    }

    public MandatoryDataMissingException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
