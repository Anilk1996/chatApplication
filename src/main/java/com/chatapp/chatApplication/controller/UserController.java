package com.chatapp.chatApplication.controller;

import com.chatapp.chatApplication.model.User;
import com.chatapp.chatApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String test(){
        return "Welcome";
    }

    @PostMapping("/register")
    Mono<ResponseEntity<User>> register(User user){
        System.err.println(user.toString());
        return userRepository.save(user).map(u -> {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        });
    }

}
