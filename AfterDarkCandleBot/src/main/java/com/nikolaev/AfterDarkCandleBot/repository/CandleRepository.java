package com.nikolaev.AfterDarkCandleBot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkCandleBot.models.Candle;

@Repository
public interface CandleRepository extends CrudRepository<Candle, Long> {
    Optional<Candle> findByChatId(long chatId);
}
