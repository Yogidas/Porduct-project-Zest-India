package com.Zest.product_assesment.Entity;

import com.Zest.product_assesment.Entity.Role;
import com.Zest.product_assesment.Entity.User;
import com.Zest.product_assesment.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUser() {
        User user = new User();
        user.setUsername("Yogi");
        user.setPassword("1234");
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("Yogi");
    }

    @Test
    void shouldSaveAndFindUserByUsername() {
        User user = new User();
        user.setUsername("Yogi");
        user.setPassword("1234");
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getPassword()).isEqualTo("1234");
    }
}