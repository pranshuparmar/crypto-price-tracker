package com.pranshu.crypto.controllers;

import com.pranshu.crypto.models.dtos.PriceResponseDTO;
import com.pranshu.crypto.services.PriceService;
import static com.pranshu.crypto.utils.Constants.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(URL.PRICES)
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping(URL.BTC)
    public ResponseEntity<PriceResponseDTO> getBitcoinPrice(@RequestParam(required = false) String date, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit) {
        return new ResponseEntity<>(priceService.getBitcoinPrice(date, offset, limit), HttpStatus.OK);
    }

    @PostMapping(URL.BTC)
    @Scheduled(initialDelay = 0, fixedRate = 30000)
    public ResponseEntity<HttpStatus> updateBitcoinPrice() {
        priceService.updateBitcoinPrice();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
