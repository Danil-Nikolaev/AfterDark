package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Shape;
import com.nikolaev.AfterDarkAPI.repositories.ShapeRepository;

@Service
public class ShapeService {

    private ShapeRepository shapeRepository;

    public ShapeService(@Autowired ShapeRepository shapeRepository) {
        this.shapeRepository = shapeRepository;
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
        shapeRepository.deleteById(id);
    }
}
