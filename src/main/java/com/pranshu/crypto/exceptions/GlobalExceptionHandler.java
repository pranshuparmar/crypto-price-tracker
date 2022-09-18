package com.pranshu.crypto.exceptions;

import com.pranshu.crypto.models.dtos.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MandatoryDataMissingException.class)
    public ResponseEntity<ErrorResponseDTO> handleMandatoryDataMissingException(MandatoryDataMissingException exception) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder().errorMessage(exception.getMessage()).build();
        HttpStatus httpStatus = exception.getHttpStatus();
        return new ResponseEntity<>(errorResponseDTO, httpStatus != null ? httpStatus : HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceAccessException(ResourceAccessException exception) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder().errorMessage(exception.getMessage()).build();
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
