package com.recipes.presentation;

import com.recipes.businesslayer.User;
import com.recipes.businesslayer.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import javax.validation.Valid;

@RestController
public class RegistrationController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    DataSource dataSource;

    @PostMapping("/api/register")
    @ResponseStatus(HttpStatus.OK)
    public void UserRegister(@Valid @RequestBody User user) {
        if(userService.findUserByUsername(user.getEmail()) == null) {
            try {
                User createdUser = new User();
                createdUser.setEmail(user.getEmail());
                createdUser.setPassword(encoder.encode(user.getPassword()));
                createdUser.setRolesAndAuthorities("USER");
                userService.saveUser(createdUser);
            } catch (RuntimeException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
