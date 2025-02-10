package com.beko.redis_demo.controller;

import com.beko.redis_demo.entity.User;
import com.beko.redis_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatusCode.valueOf(201));
    }

    @GetMapping
    public ResponseEntity<User> getById(@RequestParam Integer id) {
        User user = userService.getById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        } else {
            return new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
        }
    }
}



