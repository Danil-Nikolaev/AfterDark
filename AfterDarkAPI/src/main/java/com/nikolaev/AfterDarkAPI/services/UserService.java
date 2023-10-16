package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Basket;
import com.nikolaev.AfterDarkAPI.models.User;
import com.nikolaev.AfterDarkAPI.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private BasketService basketService;

    public UserService(@Autowired UserRepository userRepository, @Autowired BasketService basketService) {
        this.userRepository = userRepository;
        this.basketService = basketService;
    }

    public List<User> index() {
        Iterable<User> iterableUser = userRepository.findAll();
        List<User> result = new ArrayList<>();
        iterableUser.forEach(result::add);
        return result;
    }

    public User show(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        Basket basket = new Basket();
        basket.setUser(user);
        basket.setCandles(null);
        user = userRepository.save(user);
        basketService.save(basket);
        return user;
    }

    public User update(User user, long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setName(user.getName());
            existingUser.setLogin(user.getLogin());
            existingUser.setPassword(user.getPassword());
            return userRepository.save(existingUser);
        }

        return null;
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public User findByLogin(String login) {
       Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) return user.get();
        return null;
    }
}
