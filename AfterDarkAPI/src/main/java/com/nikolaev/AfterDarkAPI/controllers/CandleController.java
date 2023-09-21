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

import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.services.CandleService;

@RestController
@RequestMapping("api/afterdark/candle")
public class CandleController {
    private CandleService candleService;

    public CandleController(@Autowired CandleService candleService) {
        this.candleService = candleService;
    }

    @GetMapping
    public List<Candle> getIndex() {
        return candleService.index();
    }

    @GetMapping("{id}")
    public Candle show(@PathVariable("id") long id) {
        return candleService.show(id);
    }

    @PostMapping
    public Candle create(@ModelAttribute("candle") Candle candle) {
        return candleService.save(candle);
    }

    @PutMapping("{id}")
    public Candle update(@ModelAttribute("candle") Candle candle, @PathVariable("id") long id) {
        return candleService.update(candle, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        candleService.delete(id);
    }
}
