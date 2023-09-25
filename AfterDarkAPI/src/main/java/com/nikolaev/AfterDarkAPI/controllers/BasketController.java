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

import com.nikolaev.AfterDarkAPI.models.Basket;
import com.nikolaev.AfterDarkAPI.services.BasketService;

@RestController
@RequestMapping("/api/afterdark/basket")
public class BasketController {

    private BasketService basketService;

    public BasketController(@Autowired BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping
    public List<Basket> getIndex() {
        return basketService.index();
    }

    @GetMapping("{id}")
    public Basket show(@PathVariable("id") long id) {
        return basketService.show(id);
    }

    @PostMapping
    public Basket create(@ModelAttribute("basket") Basket basket) {
        return basketService.save(basket);
    }

    @PutMapping("{id}")
    public Basket update(@ModelAttribute("basket") Basket basket, @PathVariable("id") long id) {
        return basketService.update(basket, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        basketService.delete(id);
    }
}
