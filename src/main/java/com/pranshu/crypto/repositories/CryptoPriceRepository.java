package com.pranshu.crypto.repositories;

import com.pranshu.crypto.models.entities.CryptoPriceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CryptoPriceRepository extends PagingAndSortingRepository<CryptoPriceEntity, Long> {
    Page<CryptoPriceEntity> findBySymbolAndCreatedAtBetween(String symbol, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
