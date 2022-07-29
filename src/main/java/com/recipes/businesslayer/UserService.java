package com.recipes.businesslayer;

import com.recipes.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User toSave) {
        userRepository.save(toSave);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByEmail(username);
    }
}
