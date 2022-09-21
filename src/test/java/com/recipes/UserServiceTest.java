package com.recipes;

import com.recipes.model.User;
import com.recipes.repository.UserRepository;
import com.recipes.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceTest {

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Value("${sql.scripts.create.user}")
    String sqlAddUser;

    @Value("${sql.script.delete.user}")
    String sqlDeleteUser;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddUser);
    }

    @Test
    public void createUserService() {
        User user = new User("CamelCaseRecipe@somewhere.com", "C00k1234es", "USER");

        userService.saveUser(user);

        User savedUser = userRepository.findUserByEmail("CamelCaseRecipe@somewhere.com");

        assertNotNull(savedUser);
        assertEquals("CamelCaseRecipe@somewhere.com", savedUser.getEmail());
        assertEquals("C00k1234es", savedUser.getPassword());
        assertEquals("USER", savedUser.getRolesAndAuthorities());
    }

    @Test
    public void findUserService() {
        User findExistingUser = userService.findUserByUsername("Cook_Programmer@somewhere.com");
        User findNotExistingUser = userService.findUserByUsername("CamelCaseRecipe@somewhere.com");

        assertNotNull(findExistingUser);
        assertNull(findNotExistingUser);

        assertEquals("Cook_Programmer@somewhere.com", findExistingUser.getEmail());

    }


    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteUser);
    }

}
