package com.nikolaev.AfterDarkAPI.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nikolaev.AfterDarkAPI.models.Shape;
import com.nikolaev.AfterDarkAPI.services.ShapeService;

@RestController
@RequestMapping("api/afterdark/shape")
public class ShapeController {

    private ShapeService shapeService;

    public ShapeController(ShapeService shapeService) {
        this.shapeService = shapeService;
    }

    @GetMapping
    public List<Shape> getIndex() {
        return shapeService.index();
    }

    @GetMapping("{id}")
    public Shape show(@PathVariable("id") long id) {
        return shapeService.show(id);
    }

    @PostMapping
    public Shape create(@ModelAttribute("shape") Shape shape) {
        return shapeService.save(shape);
    }

    @PutMapping("{id}")
    public Shape update(@ModelAttribute("shape") Shape shape, @PathVariable("id") long id) {
        return shapeService.update(shape, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        shapeService.delete(id);
    }
}
