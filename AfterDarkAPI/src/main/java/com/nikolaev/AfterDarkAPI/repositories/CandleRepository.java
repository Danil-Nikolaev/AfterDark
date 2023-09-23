package com.nikolaev.AfterDarkAPI.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Candle;

@Repository
public interface CandleRepository extends CrudRepository<Candle, Long> {
    List<Candle> findAllByWickId(long id);
    List<Candle> findAllByWaxId(long id);
    List<Candle> findAllBySmellId(long id);
    List<Candle> findAllByShapeId(long id);
    List<Candle> findAllByColorShapeId(long id);
}
