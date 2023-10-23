package com.nikolaev.AfterDarkAPI.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Order;
import com.nikolaev.AfterDarkAPI.models.User;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByUser(User user);    
}
