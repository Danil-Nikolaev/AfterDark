package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Candle;

@Repository
public interface CandleRepository extends CrudRepository<Candle, Long> {
    
}
