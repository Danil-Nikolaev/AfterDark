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
import org.springframework.web.bind.annotation.RestController;

import com.nikolaev.AfterDarkAPI.models.User;
import com.nikolaev.AfterDarkAPI.services.UserService;

@RestController
@RequestMapping("/api/afterdark/user")
public class UserController {

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getIndex() {
        return userService.index();
    }

    @GetMapping("{id}")
    public User show(@PathVariable("id") long id) {
        return userService.show(id);
    }

    @GetMapping("find")
    public boolean findByLogin(String login) {
        User user = userService.findByLogin(login);
        if (user == null) return false;
        return true;
    }

    @GetMapping("findUser")
    public User findUserByLogin(String login) {
        User user = userService.findByLogin(login);
        return user;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        System.out.println(user.getLogin());
        return userService.save(user);
    }

    @PutMapping("{id}")
    public User update(@ModelAttribute("user") User user, @PathVariable("id") long id) {
        return userService.update(user, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        userService.delete(id);
    }

}
