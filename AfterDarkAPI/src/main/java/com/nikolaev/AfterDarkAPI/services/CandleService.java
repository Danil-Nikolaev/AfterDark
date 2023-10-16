package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.models.ColorShape;
import com.nikolaev.AfterDarkAPI.models.Shape;
import com.nikolaev.AfterDarkAPI.models.Smell;
import com.nikolaev.AfterDarkAPI.models.Wick;
import com.nikolaev.AfterDarkAPI.repositories.CandleRepository;

@Service
public class CandleService {
    private CandleRepository candleRepository;
    private WickService wickService;
    private SmellService smellService;
    private ShapeService shapeService;
    private ColorShapeService colorShapeService;

    

    public CandleService(CandleRepository candleRepository, WickService wickService, SmellService smellService,
            ShapeService shapeService, ColorShapeService colorShapeService) {
        this.candleRepository = candleRepository;
        this.wickService = wickService;
        this.smellService = smellService;
        this.shapeService = shapeService;
        this.colorShapeService = colorShapeService;
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
            existingCandle.setColorShape(candle.getColorShape());
            existingCandle.setCustom(candle.isCustom());
            existingCandle.setShape(candle.getShape());
            existingCandle.setSmell(candle.getSmell());
            existingCandle.setWax(candle.getWax());
            existingCandle.setWick(candle.getWick());
            return candleRepository.save(existingCandle);
        }
        return null;
    }

    public void delete(long id) {
        candleRepository.deleteById(id);
    }

    public Candle findCandle(JsonNode candle) {
        Smell smell = this.smellService.show(Long.valueOf(candle.get("smell").asText()));
        Shape shape = this.shapeService.show(Long.valueOf(candle.get("shape").asText()));
        Wick wick = this.wickService.show(Long.valueOf(candle.get("wick").asText()));
        ColorShape colorShape = this.colorShapeService.show(Long.valueOf(candle.get("colorShape").asText()));
        int price = smell.getPrice() + shape.getPrice() + wick.getPrice() + colorShape.getPrice();

        Candle localCandle = new Candle();
        localCandle.setColorShape(colorShape);
        localCandle.setShape(shape);
        localCandle.setWick(wick);
        localCandle.setSmell(smell);
        localCandle.setCustom(true);
        localCandle.setName(candle.get("name").asText());
        localCandle.setPrice(price);

        List<Candle> candles = index();
        for (Candle cyclCandle : candles) {
            if (cyclCandle.equals(localCandle)) {
                return show(cyclCandle.getId());
            }
        }
        return save(localCandle);
    }
}
