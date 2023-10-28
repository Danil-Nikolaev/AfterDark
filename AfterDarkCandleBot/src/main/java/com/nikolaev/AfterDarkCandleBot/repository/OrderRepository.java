package com.nikolaev.AfterDarkCandleBot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkCandleBot.models.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    Optional<Order> findByChatId(long chatId);
}
