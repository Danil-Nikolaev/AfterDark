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

import com.nikolaev.AfterDarkAPI.models.Gypsum;
import com.nikolaev.AfterDarkAPI.services.GypsumService;

@RestController
@RequestMapping("api/afterdark/gypsum")
public class GypsumController {

    private GypsumService gypsumService;

    public GypsumController(@Autowired GypsumService gypsumService) {
        this.gypsumService = gypsumService;
    }

    @GetMapping
    public List<Gypsum> getIndex() {
        return gypsumService.index();
    }

    @GetMapping("{id}")
    public Gypsum show(@PathVariable("id") long id) {
        return gypsumService.show(id);
    }

    @PostMapping
    public Gypsum create(@ModelAttribute("gypsum") Gypsum gypsum) {
        return gypsumService.save(gypsum);
    }

    @PutMapping("{id}")
    public Gypsum update(@ModelAttribute("gypsum") Gypsum gypsum, @PathVariable("id") long id) {
        return gypsumService.update(gypsum, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        gypsumService.delete(id);
    }
}
