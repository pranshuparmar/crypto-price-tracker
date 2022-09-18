package com.pranshu.crypto.utils;

import com.pranshu.crypto.models.dtos.CoinPriceDTO;
import com.pranshu.crypto.models.entities.CryptoPriceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class Mapper {

    public static List<CoinPriceDTO> mapCryptoPriceEntityToCoinPriceDTO(Stream<CryptoPriceEntity> cryptoPriceEntityList) {
        List<CoinPriceDTO> coinPriceDTOList = new ArrayList<>();
        cryptoPriceEntityList.forEach(cryptoPriceEntity -> {
            CoinPriceDTO coinPriceDTO = CoinPriceDTO.builder()
                    .timestamp(cryptoPriceEntity.getCreatedAt().toString())
                    .price(cryptoPriceEntity.getPrice())
                    .coin(cryptoPriceEntity.getSymbol())
                    .build();
            coinPriceDTOList.add(coinPriceDTO);
        });
        return coinPriceDTOList;
    }
}
