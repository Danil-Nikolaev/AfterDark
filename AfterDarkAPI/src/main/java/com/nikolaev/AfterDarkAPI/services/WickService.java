package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.models.Wick;
import com.nikolaev.AfterDarkAPI.repositories.CandleRepository;
import com.nikolaev.AfterDarkAPI.repositories.WickRepository;

@Service
public class WickService {

    private WickRepository wickRepository;
    private CandleRepository candleRepository;

    public WickService(@Autowired WickRepository wickRepository, @Autowired CandleRepository candleRepository) {
        this.wickRepository = wickRepository;
        this.candleRepository = candleRepository;
    }

    public List<Wick> index() {
        Iterable<Wick> iterableWick = wickRepository.findAll();
        List<Wick> result = new ArrayList<>();

        iterableWick.forEach(result::add);
        return result;
    }

    public Wick show(long id) {
        return wickRepository.findById(id).orElse(null);
    }

    public Wick save(Wick wick) {
        return wickRepository.save(wick);
    }

    public Wick update(Wick wick, long id) {
        Optional<Wick> optionalWick = wickRepository.findById(id);
        if (optionalWick.isPresent()) {
            Wick existingWick = optionalWick.get();
            existingWick.setName(wick.getName());
            existingWick.setDescription(wick.getDescription());
            existingWick.setPrice(wick.getPrice());
            existingWick.setQuanity(wick.getQuanity());
            return wickRepository.save(existingWick);
        }
        return null;
    }

    public void delete(long id) {
        List<Candle> candles = candleRepository.findAllByWickId(id);
        for (Candle candle : candles) {
            candle.setWick(null);
            candleRepository.save(candle);
        }
        wickRepository.deleteById(id);
    }

}
