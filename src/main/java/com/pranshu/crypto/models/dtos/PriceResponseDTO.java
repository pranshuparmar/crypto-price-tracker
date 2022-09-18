package com.pranshu.crypto.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PriceResponseDTO {
    private String url; // current url
    private String next; // next url in pagination
    private Long count; // total no of records for the given query
    private List<CoinPriceDTO> data; // list of price objects
}
