package com.pranshu.crypto.models.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CoinPriceDTO {
    private String timestamp; // timestamp of the price
    private Integer price; // price of the coin
    private String coin; // coin symbol
}
