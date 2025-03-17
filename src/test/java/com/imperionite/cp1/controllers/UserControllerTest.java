package com.imperionite.cp1.controllers;

import com.imperionite.cp1.dtos.UserResponseDTO;
import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentUser() {
        User user = new User("testUser", "password");
        user.setId(1L);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(user));

        ResponseEntity<UserResponseDTO> response = userController.getCurrentUser(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testAllUsersAdmin() {
        User adminUser = new User("admin", "adminPass");
        adminUser.setIsAdmin(true);
        when(userDetails.getUsername()).thenReturn("admin");
        when(userService.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        List<User> userList = new ArrayList<>();
        userList.add(new User("user1","pass1"));
        when(userService.allUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = userController.allUsers(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testAllUsersNotAdmin() {
        User normalUser = new User("user", "pass");
        normalUser.setIsAdmin(false);
        when(userDetails.getUsername()).thenReturn("user");
        when(userService.findByUsername("user")).thenReturn(Optional.of(normalUser));

        ResponseEntity<List<User>> response = userController.allUsers(userDetails);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}