package com.imperionite.cp1.services;

import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("user1", "pass1"));
        users.add(new User("user2", "pass2"));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.allUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByUsername() {
        User user = new User("testUser", "password");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("testUser");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testGetUserById() {
        User user = new User("testUser", "password");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }
}