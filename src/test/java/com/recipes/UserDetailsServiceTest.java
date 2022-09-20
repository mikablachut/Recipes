package com.recipes;

import com.recipes.repository.UserRepository;
import com.recipes.security.UserDetailsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserDetailsServiceTest {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JdbcTemplate jdbc;

    @Value("${sql.scripts.create.user}")
    String sqlAddUser;

    @Value("${sql.script.delete.user}")
    String sqlDeleteUser;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddUser);
    }

    @Test
    public void loadUserDetailsService() {
        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("USER"));

        UserDetails user = userDetailsService.loadUserByUsername("Cook_Programmer@somewhere.com");

        assertNotNull(user);

        assertEquals("Cook_Programmer@somewhere.com", user.getUsername());
        assertEquals("password1234", user.getPassword());

        assertIterableEquals(grantedAuthorities, user.getAuthorities());

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    public void loadNotExistUserDetailsService() {
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("CamelCaseRecipe@somewhere.com");
        });

        String expectedMessage = "Not found: CamelCaseRecipe@somewhere.com";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteUser);
    }
}
