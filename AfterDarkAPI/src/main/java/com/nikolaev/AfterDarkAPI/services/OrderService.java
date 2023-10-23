package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Order;
import com.nikolaev.AfterDarkAPI.models.User;
import com.nikolaev.AfterDarkAPI.repositories.OrderRepository;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private UserService userService;

    public OrderService(@Autowired OrderRepository orderRepository, @Autowired UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    public List<Order> index() {
        ArrayList<Order> result = new ArrayList<>();
        Iterable<Order> source = orderRepository.findAll();
        source.forEach(result::add);
        return result;
    }

    public Order show(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order save(Order order) {
        orderRepository.save(order);
        return order;
    }

    public Order update(Order order, long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order existingOrder = optionalOrder.get();
            existingOrder.setAddress(order.getAddress());
            existingOrder.setCandles(order.getCandles());
            existingOrder.setCommunication(order.getCommunication());
            existingOrder.setDateOfDelivery(order.getDateOfDelivery());
            existingOrder.setDateOfPurchase(order.getDateOfPurchase());
            existingOrder.setPaid(order.isPaid());
            existingOrder.setPaymentMethod(order.getPaymentMethod());
            existingOrder.setPrice(order.getPrice());
            existingOrder.setPurchaseService(order.getPurchaseService());
            existingOrder.setStageOfWork(order.getStageOfWork());
            return orderRepository.save(existingOrder);
        }
        return null;
    }

    public void delete(long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> findAllByUser(String login) {
        User user = userService.findByLogin(login);
        return orderRepository.findByUser(user);
    }

}
