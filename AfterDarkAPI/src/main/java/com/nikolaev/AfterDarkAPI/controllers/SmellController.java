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

import com.nikolaev.AfterDarkAPI.models.Smell;
import com.nikolaev.AfterDarkAPI.services.SmellService;

@RestController
@RequestMapping("api/afterdark/smell")
public class SmellController {

    private SmellService smellService;

    public SmellController(@Autowired SmellService smellService) {
        this.smellService = smellService;
    }

    @GetMapping
    public List<Smell> getIndex() {
        return smellService.index();
    }

    @GetMapping("{id}")
    public Smell show(@PathVariable("id") long id) {
        return smellService.show(id);
    }

    @PostMapping
    public Smell create(@ModelAttribute("smell") Smell smell) {
        return smellService.save(smell);
    }

    @PutMapping("{id}")
    public Smell update(@ModelAttribute("smell") Smell smell, @PathVariable("id") long id) {
        return smellService.update(smell, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        smellService.delete(id);
    }
}
