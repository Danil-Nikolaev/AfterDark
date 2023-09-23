package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.models.Wax;
import com.nikolaev.AfterDarkAPI.repositories.CandleRepository;
import com.nikolaev.AfterDarkAPI.repositories.WaxRepository;

@Service
public class WaxService {
    private WaxRepository waxRepository;
    private CandleRepository candleRepository;

    public WaxService(@Autowired WaxRepository waxRepository, @Autowired CandleRepository candleRepository) {
        this.waxRepository = waxRepository;
        this.candleRepository = candleRepository;
    }

    public List<Wax> index() {
        ArrayList<Wax> result = new ArrayList<>();
        Iterable<Wax> source = waxRepository.findAll();
        source.forEach(result::add);
        return result;
    }

    public Wax show(long id) {
        return waxRepository.findById(id).orElse(null);
    }

    public Wax save(Wax wax) {
        waxRepository.save(wax);
        return wax;
    }

    public Wax update(Wax wax, long id) {
        Optional<Wax> optionalWax = waxRepository.findById(id);
        if (optionalWax.isPresent()) {
            Wax existingWax = optionalWax.get();
            existingWax.setName(wax.getName());
            existingWax.setDescription(wax.getDescription());
            existingWax.setPrice(wax.getPrice());
            existingWax.setQuanity(wax.getQuanity());
            return waxRepository.save(existingWax);
        }
        return null;
    }

    public void delete(long id) {
        List<Candle> candles = candleRepository.findAllByWaxId(id);
        for (Candle candle : candles) {
            candle.setWax(null);
            candleRepository.save(candle);
        }
        waxRepository.deleteById(id);
    }
}
