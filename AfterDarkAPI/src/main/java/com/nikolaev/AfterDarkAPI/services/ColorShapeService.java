package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.ColorShape;
import com.nikolaev.AfterDarkAPI.repositories.ColorShapeRepository;

@Service
public class ColorShapeService {

    private ColorShapeRepository colorShapeRepository;

    public ColorShapeService(@Autowired ColorShapeRepository colorShapeRepository) {
        this.colorShapeRepository = colorShapeRepository;
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
        colorShapeRepository.deleteById(id);
    }
}
