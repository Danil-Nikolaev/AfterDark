package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.models.Smell;
import com.nikolaev.AfterDarkAPI.repositories.CandleRepository;
import com.nikolaev.AfterDarkAPI.repositories.SmellRepository;

@Service
public class SmellService {

    private SmellRepository smellRepository;
    private CandleRepository candleRepository;

    public SmellService(@Autowired SmellRepository smellRepository, @Autowired CandleRepository candleRepository) {
        this.smellRepository = smellRepository;
        this.candleRepository = candleRepository;
    }

    public List<Smell> index() {
        Iterable<Smell> iterableSmell = smellRepository.findAll();
        List<Smell> result = new ArrayList<>();
        iterableSmell.forEach(result::add);
        return result;
    }

    public Smell show(long id) {
        return smellRepository.findById(id).orElse(null);
    }

    public Smell save(Smell smell) {
        return smellRepository.save(smell);
    }

    public Smell update(Smell smell, long id) {
        Optional<Smell> optionalSmell = smellRepository.findById(id);
        if (optionalSmell.isPresent()) {
            Smell existingSmell = optionalSmell.get();
            existingSmell.setName(smell.getName());
            existingSmell.setDescription(smell.getDescription());
            existingSmell.setPrice(smell.getPrice());
            existingSmell.setQuanity(smell.getQuanity());
            return smellRepository.save(existingSmell);
        }

        return null;
    }

    public void delete(long id) {
        List<Candle> candles = candleRepository.findAllBySmellId(id);
        for (Candle candle : candles) {
            candle.setSmell(null);
            candleRepository.save(candle);
        }
        smellRepository.deleteById(id);
    }

}
