package com.pranshu.crypto.services;

import com.pranshu.crypto.configs.LogbookConfiguration;
import com.pranshu.crypto.exceptions.DateFormatMismatchException;
import com.pranshu.crypto.exceptions.MandatoryDataMissingException;
import com.pranshu.crypto.models.dtos.CoinPriceDTO;
import com.pranshu.crypto.models.dtos.CoingeckoPriceResponseDTO;
import com.pranshu.crypto.models.dtos.PriceResponseDTO;
import com.pranshu.crypto.models.entities.CryptoPriceEntity;
import com.pranshu.crypto.repositories.CryptoPriceRepository;
import static com.pranshu.crypto.utils.Constants.*;
import static com.pranshu.crypto.utils.Mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Service
public class PriceService {

    private final ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
    private final RestTemplate restTemplate = new RestTemplate(factory);

    @Autowired
    private LogbookConfiguration logbookConfiguration;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CryptoPriceRepository cryptoPriceRepository;

    @Value("${coingecko.url.base}")
    private String coingeckoBaseUrl;

    @Value("${alert.email}")
    private String alertEmail;

    @Value("${price.max.bitcoin}")
    private Integer bitcoinMaxPrice;

    @Value("${price.min.bitcoin}")
    private Integer bitcoinMinPrice;

    public PriceResponseDTO getBitcoinPrice(String date, Integer offset, Integer limit) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateToConsider;
        if(date == null || date.isBlank()) {
            dateToConsider = LocalDate.now(); // default to current date if it is not passed
        } else {
            try {
                dateToConsider = LocalDate.parse(date, dateTimeFormatter);
            } catch (DateTimeParseException e) {
                throw new DateFormatMismatchException("Date passed is not in valid format");
            }
        }
        if(offset == null) {
            offset = 0;
        }
        if(limit == null) {
            limit = 100;
        }
        log.info("date: {}, offset: {}, limit: {}", date, offset, limit);
        PriceResponseDTO priceResponseDTO = new PriceResponseDTO();
        List<CryptoPriceEntity> cryptoPriceEntityList = cryptoPriceRepository.findBySymbolAndCreatedAtBetween(SYMBOL.BITCOIN, dateToConsider.atStartOfDay(), dateToConsider.plusDays(1).atStartOfDay());
        List<CoinPriceDTO> coinPriceDTOList = mapCryptoPriceEntityToCoinPriceDTO(cryptoPriceEntityList);
        priceResponseDTO.setData(coinPriceDTOList);
        return priceResponseDTO;
    }

    public void updateBitcoinPrice() {
        // Fetch the latest bitcoin price in USD
        restTemplate.getInterceptors().add(logbookConfiguration.logbookClientHttpRequestInterceptor());
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(coingeckoBaseUrl + URL.SIMPLE_PRICE)
                .queryParam(QUERY_PARAM.IDS, SYMBOL.BITCOIN)
                .queryParam(QUERY_PARAM.VS_CURRENCIES, CURRENCY.USD)
                .toUriString()
                ;
        log.debug("Calling Coingecko price API");
        ResponseEntity<CoingeckoPriceResponseDTO> priceResponse;
        try {
            priceResponse = restTemplate.exchange(urlTemplate, HttpMethod.GET, null, CoingeckoPriceResponseDTO.class);
        } catch (ResourceAccessException ex) {
            throw new ResourceAccessException("Unable to access Coingecko API");
        }

        if(priceResponse.getStatusCode().equals(HttpStatus.OK)) {
            log.debug("Coingecko price API response received successfully");
            CoingeckoPriceResponseDTO coingeckoPriceResponseDTO = priceResponse.getBody();
            if(coingeckoPriceResponseDTO == null || coingeckoPriceResponseDTO.getBitcoin() == null || coingeckoPriceResponseDTO.getBitcoin().getUsd() == null) {
                log.error("Mandatory fields are missing in Coingecko price API response");
                throw new MandatoryDataMissingException(ERROR.INSUFFICIENT_DATA);
            }
            // Store the result in database
            Integer bitcoinPrice = coingeckoPriceResponseDTO.getBitcoin().getUsd();
            CryptoPriceEntity cryptoPriceEntity = CryptoPriceEntity.builder().symbol(SYMBOL.BITCOIN).price(bitcoinPrice).build();
            cryptoPriceEntity = cryptoPriceRepository.save(cryptoPriceEntity);
            log.trace("Coingecko price API response saved successfully");

            // Check max min conditions
            if (bitcoinPrice > bitcoinMaxPrice) {
                emailService.sendEmail(alertEmail, "Bitcoin price above threshold", "Bitcoin price exceeds threshold with current value of USD " + bitcoinPrice + " at " + cryptoPriceEntity.getCreatedAt() + " against threshold value of USD " + bitcoinMaxPrice);
            } else if (bitcoinPrice < bitcoinMinPrice) {
                emailService.sendEmail(alertEmail, "Bitcoin price below threshold", "Bitcoin price fell below threshold with current value of USD " + bitcoinPrice + " at " + cryptoPriceEntity.getCreatedAt() + " against threshold value of USD " + bitcoinMinPrice);
            }
        } else {
            log.error("Unexpected response received while calling Coingecko price API: {}", priceResponse);
            throw new MandatoryDataMissingException(priceResponse.getStatusCode(), ERROR.INSUFFICIENT_DATA);
        }
    }

}
