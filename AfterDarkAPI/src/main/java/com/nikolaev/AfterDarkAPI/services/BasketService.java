package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Basket;
import com.nikolaev.AfterDarkAPI.repositories.BasketRepository;

@Service
public class BasketService {

    private BasketRepository basketRepository;

    public BasketService(@Autowired BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public List<Basket> index() {
        ArrayList<Basket> result = new ArrayList<>();
        Iterable<Basket> source = basketRepository.findAll();
        source.forEach(result::add);
        return result;
    }

    public Basket show(long id) {
        return basketRepository.findById(id).orElse(null);
    }

    public Basket save(Basket basket) {
        basketRepository.save(basket);
        return basket;
    }

    public Basket update(Basket basket, long id) {
        Optional<Basket> optionalBasket = basketRepository.findById(id);
        if (optionalBasket.isPresent()) {
            Basket existingBasket = optionalBasket.get();
            existingBasket.setCandles(basket.getCandles());
            return basketRepository.save(existingBasket);
        }
        return null;
    }

    public void delete(long id) {
        basketRepository.deleteById(id);
    }
}
