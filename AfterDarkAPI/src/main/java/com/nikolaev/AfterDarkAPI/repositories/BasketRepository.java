package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Basket;

@Repository
public interface BasketRepository extends CrudRepository<Basket, Long>{
    
}
