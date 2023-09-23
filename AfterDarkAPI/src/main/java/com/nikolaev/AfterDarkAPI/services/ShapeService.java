package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.models.Shape;
import com.nikolaev.AfterDarkAPI.repositories.CandleRepository;
import com.nikolaev.AfterDarkAPI.repositories.ShapeRepository;

@Service
public class ShapeService {

    private ShapeRepository shapeRepository;
    private CandleRepository candleRepository;

    public ShapeService(@Autowired ShapeRepository shapeRepository, @Autowired CandleRepository candleRepository) {
        this.shapeRepository = shapeRepository;
        this.candleRepository = candleRepository;
    }

    public List<Shape> index() {
        Iterable<Shape> iterableShape = shapeRepository.findAll();
        List<Shape> result = new ArrayList<>();
        iterableShape.forEach(result::add);
        return result;
    }

    public Shape show(long id) {
        return shapeRepository.findById(id).orElse(null);
    }

    public Shape save(Shape shape) {
        return shapeRepository.save(shape);
    }

    public Shape update(Shape shape, long id) {
        Optional<Shape> optionalShape = shapeRepository.findById(id);
        if (optionalShape.isPresent()) {
            Shape existingShape = optionalShape.get();
            existingShape.setName(shape.getName());
            existingShape.setDescription(shape.getDescription());
            existingShape.setPrice(shape.getPrice());
            existingShape.setQuanity(shape.getQuanity());
            existingShape.setVolume(shape.getVolume());
            return shapeRepository.save(existingShape);
        }
        return null;
    }

    public void delete(long id) {
        List<Candle> candles = candleRepository.findAllByShapeId(id);
        for (Candle candle : candles) {
            candle.setShape(null);
            candleRepository.save(candle);
        }
        shapeRepository.deleteById(id);
    }
}
