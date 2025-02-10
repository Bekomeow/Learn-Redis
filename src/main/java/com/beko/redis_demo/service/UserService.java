package com.beko.redis_demo.service;

import com.beko.redis_demo.entity.User;
import com.beko.redis_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    @Cacheable(value = "users", key = "#id")
    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(null);
    }
}
