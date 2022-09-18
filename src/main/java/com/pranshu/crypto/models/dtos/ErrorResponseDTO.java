package com.pranshu.crypto.models.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponseDTO {
    private String errorMessage;
}
