package com.pranshu.crypto.repositories;

import com.pranshu.crypto.models.entities.CryptoPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPriceEntity, Long> {
    List<CryptoPriceEntity> findBySymbolAndCreatedAtBetween(String symbol, LocalDateTime from, LocalDateTime to);
}
