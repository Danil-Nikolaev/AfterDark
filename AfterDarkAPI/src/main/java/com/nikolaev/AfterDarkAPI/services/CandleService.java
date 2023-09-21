package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.repositories.CandleRepository;

@Service
public class CandleService {
    private CandleRepository candleRepository;

    public CandleService(@Autowired CandleRepository candleRepository) {
        this.candleRepository = candleRepository;
    }

    public List<Candle> index() {
        ArrayList<Candle> result = new ArrayList<>();
        Iterable<Candle> source = candleRepository.findAll();
        source.forEach(result::add);
        return result;
    }

    public Candle show(long id) {
        return candleRepository.findById(id).orElse(null);
    }

    public Candle save(Candle candle) {
        candleRepository.save(candle);
        return candle;
    }

    public Candle update(Candle candle, long id) {
        Optional<Candle> optionalCandle = candleRepository.findById(id);
        if (optionalCandle.isPresent()) {
            Candle existingCandle = optionalCandle.get();
            existingCandle.setName(candle.getName());
            existingCandle.setDescription(candle.getDescription());
            existingCandle.setPrice(candle.getPrice());
            existingCandle.setQuanity(candle.getQuanity());
            return candleRepository.save(existingCandle);
        }
        return null;
    }

    public void delete(long id) {
        candleRepository.deleteById(id);
    }
}
