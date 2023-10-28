package com.nikolaev.AfterDarkCandleBot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nikolaev.AfterDarkCandleBot.models.Candle;
import com.nikolaev.AfterDarkCandleBot.repository.CandleRepository;

@Service
public class CandleService {
    
    @Autowired
    private CandleRepository candleRepository;

    @Transactional
    public void updateShapeIdByChatId(long chatId, long shapeId) {
        Optional<Candle> optionalCandle = candleRepository.findByChatId(chatId);

        if (optionalCandle.isPresent()) {
            Candle candle = optionalCandle.get();
            candle.setShapeId(shapeId);
            candleRepository.save(candle);
        }
    }

    @Transactional
    public void updateColorShapeIdByChatId(long chatId, long colorShapeId) {
        Optional<Candle> optionalCandle = candleRepository.findByChatId(chatId);

        if (optionalCandle.isPresent()) {
            Candle candle = optionalCandle.get();
            candle.setColorShapeId(colorShapeId);
            candleRepository.save(candle);
        }
    }

    @Transactional
    public void updateSmellIdByChatId(long chatId, long smellId) {
        Optional<Candle> optionalCandle = candleRepository.findByChatId(chatId);

        if (optionalCandle.isPresent()) {
            Candle candle = optionalCandle.get();
            candle.setSmellId(smellId);
            candleRepository.save(candle);
        }
    }

    @Transactional
    public void updateWickIdByChatId(long chatId, long wickId) {
        Optional<Candle> optionalCandle = candleRepository.findByChatId(chatId);

        if (optionalCandle.isPresent()) {
            Candle candle = optionalCandle.get();
            candle.setWickId(wickId);
            candleRepository.save(candle);
        }
    }

    @Transactional
    public Candle findByChatId(long chatId) {
        return this.candleRepository.findByChatId(chatId).orElse(null);
    }

    @Transactional
    public Candle save(long chatId, Candle candle) {
        return this.candleRepository.save(candle);
    }

}
