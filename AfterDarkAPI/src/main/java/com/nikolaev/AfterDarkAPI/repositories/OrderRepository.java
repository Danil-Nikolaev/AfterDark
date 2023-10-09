package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    
}
