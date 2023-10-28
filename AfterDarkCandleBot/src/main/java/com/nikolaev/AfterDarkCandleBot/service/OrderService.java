package com.nikolaev.AfterDarkCandleBot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nikolaev.AfterDarkCandleBot.models.Order;
import com.nikolaev.AfterDarkCandleBot.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order findByChatId(long chatId) {
        return this.orderRepository.findByChatId(chatId).orElse(null);
    }

    @Transactional
    public Order updateName(long chatId, String name) {
        Optional<Order> optionalOrder = this.orderRepository.findByChatId(chatId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setName(name);
            return this.orderRepository.save(order);
        }
        return null;
    }

    @Transactional
    public Order updatePhone(long chatId, String phone) {
        Optional<Order> optionalOrder = this.orderRepository.findByChatId(chatId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setPhone(phone);
            return this.orderRepository.save(order);
        }
        return null;
    }

    @Transactional
    public Order updateAddress(long chatId, String address) {
        Optional<Order> optionalOrder = this.orderRepository.findByChatId(chatId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setAddress(address);
            return this.orderRepository.save(order);
        }
        return null;
    }

    @Transactional
    public Order save(long chatId, Order order) {
        return this.orderRepository.save(order);
    }

}
