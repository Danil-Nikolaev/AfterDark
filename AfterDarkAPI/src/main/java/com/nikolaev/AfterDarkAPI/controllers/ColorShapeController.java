package com.nikolaev.AfterDarkAPI.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nikolaev.AfterDarkAPI.models.ColorShape;
import com.nikolaev.AfterDarkAPI.services.ColorShapeService;

@RestController
@RequestMapping("api/afterdark/colorrshape")
public class ColorShapeController {

    private ColorShapeService colorShapeService;

    public ColorShapeController(@Autowired ColorShapeService colorShapeService) {
        this.colorShapeService = colorShapeService;
    }

    @GetMapping
    public List<ColorShape> getIndex() {
        return colorShapeService.index();
    }

    @GetMapping("{id}")
    public ColorShape show(@PathVariable("id") long id) {
        return colorShapeService.show(id);
    }

    @PostMapping
    public ColorShape create(@ModelAttribute("colorShape") ColorShape colorShape) {
        return colorShapeService.save(colorShape);
    }

    @PutMapping("{id}")
    public ColorShape update(@ModelAttribute("colorShape") ColorShape colorShape, @PathVariable("id") long id) {
        return colorShapeService.update(colorShape, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        colorShapeService.delete(id);
    }
}
