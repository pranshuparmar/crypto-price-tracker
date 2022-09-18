package com.pranshu.crypto.utils;

import com.pranshu.crypto.models.dtos.CoinPriceDTO;
import com.pranshu.crypto.models.entities.CryptoPriceEntity;

import java.util.ArrayList;
import java.util.List;

public final class Mapper {

    public static List<CoinPriceDTO> mapCryptoPriceEntityToCoinPriceDTO(List<CryptoPriceEntity> cryptoPriceEntityList) {
        List<CoinPriceDTO> coinPriceDTOList = new ArrayList<>();
        for(CryptoPriceEntity cryptoPriceEntity : cryptoPriceEntityList) {
            CoinPriceDTO coinPriceDTO = CoinPriceDTO.builder()
                    .timestamp(cryptoPriceEntity.getCreatedAt().toString())
                    .price(cryptoPriceEntity.getPrice())
                    .coin(cryptoPriceEntity.getSymbol())
                    .build();
            coinPriceDTOList.add(coinPriceDTO);
        }
        return coinPriceDTOList;
    }
}
