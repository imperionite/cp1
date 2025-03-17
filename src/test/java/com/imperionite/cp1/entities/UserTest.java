package com.imperionite.cp1.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User("testUser", "password123");

        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertTrue(user.getIsActive());
        assertFalse(user.getIsAdmin());
        assertNull(user.getId());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void testUserAllArgsConstructor() {
        Date now = new Date(System.currentTimeMillis());
        User user = new User(1L, "testUser", "password123", true, false, now, now);

        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertTrue(user.getIsActive());
        assertFalse(user.getIsAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testUserSetters() {
        User user = new User();

        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setIsActive(false);
        user.setIsAdmin(true);
        Date now = new Date(System.currentTimeMillis());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertFalse(user.getIsActive());
        assertTrue(user.getIsAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testDeactivate() {
        User user = new User("testUser", "password123");
        user.deactivate();
        assertFalse(user.getIsActive());
    }
}