package com.imperionite.cp1.controllers;

import com.imperionite.cp1.services.DeductionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeductionsControllerTest {

    @Mock
    private DeductionsService deductionsService;

    @Mock
    private Logger logger;

    @InjectMocks
    private DeductionsController deductionsController;

    private UserDetails employeeUser;
    private UserDetails adminUser;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        Collection<GrantedAuthority> employeeAuthorities = new ArrayList<>();
        employeeAuthorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        employeeUser = new User("employee123", "password", employeeAuthorities);

        Collection<GrantedAuthority> adminAuthorities = new ArrayList<>();
        adminAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        adminUser = new User("admin123", "password", adminAuthorities);

        startDate = LocalDate.of(2024, 1, 1); // Monday
        endDate = LocalDate.of(2024, 1, 7);   // Sunday

    }

    @Test
    void calculateWeeklySssDeduction_employee_success() {
        when(deductionsService.calculateWeeklySssDeduction("employee123", startDate, endDate))
                .thenReturn(new BigDecimal("100.00"));

        ResponseEntity<?> response = deductionsController.calculateWeeklySssDeduction(employeeUser, null, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> body = (Map<String, BigDecimal>) response.getBody();
        assertEquals(new BigDecimal("100.00"), body.get("weekly_sss_deduction"));
    }

    @Test
    void calculateWeeklySssDeduction_admin_success() {
        when(deductionsService.calculateWeeklySssDeduction("employee456", startDate, endDate))
                .thenReturn(new BigDecimal("150.00"));

        ResponseEntity<?> response = deductionsController.calculateWeeklySssDeduction(adminUser, "employee456", startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> body = (Map<String, BigDecimal>) response.getBody();
        assertEquals(new BigDecimal("150.00"), body.get("weekly_sss_deduction"));
    }

    @Test
    void calculateWeeklySssDeduction_unauthorized() {
        ResponseEntity<?> response = deductionsController.calculateWeeklySssDeduction(null, "employee123", startDate, endDate);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void calculateWeeklySssDeduction_forbidden() {
        ResponseEntity<?> response = deductionsController.calculateWeeklySssDeduction(employeeUser, "employee456", startDate, endDate);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void calculateWeeklySssDeduction_badRequest() {
        when(deductionsService.calculateWeeklySssDeduction("employee123", startDate, endDate))
                .thenThrow(new IllegalArgumentException("Invalid date"));

        ResponseEntity<?> response = deductionsController.calculateWeeklySssDeduction(employeeUser, null, startDate, endDate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date", response.getBody());
    }

    @Test
    void calculateWeeklyPhilHealthDeduction_employee_success() {
        when(deductionsService.calculateWeeklyPhilHealthDeduction("employee123", startDate, endDate))
                .thenReturn(new BigDecimal("50.00"));

        ResponseEntity<?> response = deductionsController.calculateWeeklyPhilHealthDeduction(employeeUser, null, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> body = (Map<String, BigDecimal>) response.getBody();
        assertEquals(new BigDecimal("50.00"), body.get("weekly_philhealth_deduction"));
    }

    @Test
    void calculateWeeklyPagIbigDeduction_employee_success() {
        when(deductionsService.calculateWeeklyPagIbigDeduction("employee123", startDate, endDate))
                .thenReturn(new BigDecimal("25.00"));

        ResponseEntity<?> response = deductionsController.calculateWeeklyPagIbigDeduction(employeeUser, null, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> body = (Map<String, BigDecimal>) response.getBody();
        assertEquals(new BigDecimal("25.00"), body.get("weekly_pagibig_deduction"));
    }

    @Test
    void calculateWeeklyWithholdingTax_employee_success() {
        when(deductionsService.calculateWeeklyWithholdingTax("employee123", startDate, endDate))
                .thenReturn(new BigDecimal("75.00"));

        ResponseEntity<?> response = deductionsController.calculateWeeklyWithholdingTax(employeeUser, startDate, endDate, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> body = (Map<String, BigDecimal>) response.getBody();
        assertEquals(new BigDecimal("75.00"), body.get("weekly_withholding_tax"));
    }
    @Test
    void calculateWeeklyWithholdingTax_admin_success() {
        when(deductionsService.calculateWeeklyWithholdingTax("employee456", startDate, endDate))
                .thenReturn(new BigDecimal("75.00"));

        ResponseEntity<?> response = deductionsController.calculateWeeklyWithholdingTax(adminUser, startDate, endDate, "employee456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> body = (Map<String, BigDecimal>) response.getBody();
        assertEquals(new BigDecimal("75.00"), body.get("weekly_withholding_tax"));
    }
}