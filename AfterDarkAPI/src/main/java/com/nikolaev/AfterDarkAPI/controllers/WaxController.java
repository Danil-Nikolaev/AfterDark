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

import com.nikolaev.AfterDarkAPI.models.Wax;
import com.nikolaev.AfterDarkAPI.services.WaxService;

@RestController
@RequestMapping("/api/afterdark/wax")
public class WaxController {

    private WaxService waxService;

    public WaxController(@Autowired WaxService waxService) {
        this.waxService = waxService;
    }

    @GetMapping
    public List<Wax> getIndex() {
        return waxService.index();
    }

    @GetMapping("/{id}")
    public Wax show(@PathVariable("id") long id) {
        return waxService.show(id);
    }

    @PostMapping
    public Wax create(@ModelAttribute("wax") Wax wax) {
        return waxService.save(wax);
    }

    @PutMapping("{id}")
    public Wax update(@ModelAttribute("wax") Wax wax, @PathVariable("id") long id) {
        return waxService.update(wax, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        waxService.delete(id);
    }
}
