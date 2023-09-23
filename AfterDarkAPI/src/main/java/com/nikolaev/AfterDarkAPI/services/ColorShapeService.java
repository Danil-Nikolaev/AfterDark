package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.models.ColorShape;
import com.nikolaev.AfterDarkAPI.repositories.CandleRepository;
import com.nikolaev.AfterDarkAPI.repositories.ColorShapeRepository;

@Service
public class ColorShapeService {

    private ColorShapeRepository colorShapeRepository;
    private CandleRepository candleRepository;

    public ColorShapeService(@Autowired ColorShapeRepository colorShapeRepository, @Autowired CandleRepository candleRepository) {
        this.colorShapeRepository = colorShapeRepository;
        this.candleRepository = candleRepository;
    }

    public List<ColorShape> index() {
        Iterable<ColorShape> iterableColor = colorShapeRepository.findAll();
        List<ColorShape> result = new ArrayList<>();
        iterableColor.forEach(result::add);
        return result;
    }

    public ColorShape show(long id) {
        return colorShapeRepository.findById(id).orElse(null);
    }

    public ColorShape save(ColorShape colorShape) {
        return colorShapeRepository.save(colorShape);
    }

    public ColorShape update(ColorShape colorShape, long id) {
        Optional<ColorShape> optionalColor = colorShapeRepository.findById(id);
        if (optionalColor.isPresent()) {
            ColorShape existingColorShape = optionalColor.get();
            existingColorShape.setName(colorShape.getName());
            existingColorShape.setDescription(colorShape.getDescription());
            existingColorShape.setPrice(colorShape.getPrice());
            return colorShapeRepository.save(existingColorShape);
        }
        return null;
    }

    public void delete(long id) {
        List<Candle> candles = candleRepository.findAllByColorShapeId(id);
        for (Candle candle : candles) {
            candle.setColorShape(null);
            candleRepository.save(candle);
        }
        colorShapeRepository.deleteById(id);
    }
}
