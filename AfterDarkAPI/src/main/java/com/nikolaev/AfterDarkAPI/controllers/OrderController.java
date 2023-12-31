package com.nikolaev.AfterDarkAPI.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nikolaev.AfterDarkAPI.models.Order;
import com.nikolaev.AfterDarkAPI.services.OrderService;

@RestController
@RequestMapping("/api/afterdark/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(@Autowired OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getIndex() {
        return orderService.index();
    }

    @GetMapping("{id}")
    public Order show(@PathVariable("id") long id) {
        return orderService.show(id);
    }

    @GetMapping("findByUser")
    public List<Order> findAllByUser(@RequestParam String login) {
        return orderService.findAllByUser(login);
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.save(order);
    }

    @PutMapping("{id}")
    public Order update(@ModelAttribute("order") Order order, @PathVariable("id") long id) {
        return orderService.update(order, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        orderService.delete(id);
    }
}
