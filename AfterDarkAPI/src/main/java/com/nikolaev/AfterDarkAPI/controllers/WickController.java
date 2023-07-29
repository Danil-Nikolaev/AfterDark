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

import com.nikolaev.AfterDarkAPI.models.Wick;
import com.nikolaev.AfterDarkAPI.services.WickService;

@RestController
@RequestMapping("api/afterdark/wick")
public class WickController {
    
    private WickService wickService;

    public WickController(@Autowired WickService wickService) {
        this.wickService = wickService;
    }

    @GetMapping
    public List<Wick> getIndex() {
        return wickService.index();
    }

    @GetMapping("{id}")
    public Wick show(@PathVariable("id") long id) {
        return wickService.show(id);
    }

    @PostMapping
    public Wick create(@ModelAttribute("wick") Wick wick) {
        return wickService.save(wick);
    }

    @PutMapping("{id}")
    public Wick update(@ModelAttribute("wick") Wick wick, @PathVariable("id") long id) {
        return wickService.update(wick, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        wickService.delete(id);
    }
}
