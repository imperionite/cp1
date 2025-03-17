package com.imperionite.cp1.controllers;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.imperionite.cp1.services.SalaryService;
import com.imperionite.cp1.services.DeductionsService;
import com.imperionite.cp1.services.AttendanceService;

class SalaryControllerTest {

    @Mock
    private SalaryService salaryService;

    @Mock
    private DeductionsService deductionsService;

    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private SalaryController salaryController;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the UserDetails to return a predefined employee number
        when(userDetails.getUsername()).thenReturn("EMP001");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCalculateGrossWeeklySalary_InvalidDateRange() {
        LocalDate startDate = LocalDate.of(2024, 1, 2); // Invalid start date (Tuesday)
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Valid end date (Sunday)

        // Call the controller method
        ResponseEntity<?> response = salaryController.calculateGrossWeeklySalary(userDetails, startDate, endDate);

        // Assert that the response has a BAD_REQUEST status and appropriate error message
        assertEquals(400, response.getStatusCodeValue()); // Expecting 400 BAD_REQUEST
        assertEquals("Start date must be a Monday and end date must be a Sunday.", response.getBody());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCalculateNetWeeklySalary_InvalidDateRange() {
        LocalDate startDate = LocalDate.of(2024, 1, 2); // Invalid start date (Tuesday)
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Valid end date (Sunday)

        // Call the controller method
        ResponseEntity<?> response = salaryController.calculateNetWeeklySalary(userDetails, "EMP001", startDate, endDate);

        // Assert that the response has a BAD_REQUEST status and appropriate error message
        assertEquals(400, response.getStatusCodeValue()); // Expecting 400 BAD_REQUEST
        assertEquals("Start date must be a Monday and end date must be a Sunday.", response.getBody());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCalculateGrossWeeklySalary_ValidDateRange() {
        LocalDate startDate = LocalDate.of(2024, 1, 1); // Valid start date (Monday)
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Valid end date (Sunday)
        BigDecimal expectedSalary = new BigDecimal("1000.00");

        // Mock the service to return a predefined salary
        when(salaryService.calculateGrossWeeklySalary("EMP001", startDate, endDate)).thenReturn(expectedSalary);

        // Call the controller method
        ResponseEntity<?> response = salaryController.calculateGrossWeeklySalary(userDetails, startDate, endDate);

        // Assert that the response has an OK status and the correct gross weekly salary
        assertEquals(200, response.getStatusCodeValue()); // Expecting 200 OK
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> responseBody = (Map<String, BigDecimal>) response.getBody();
        assertEquals(expectedSalary, responseBody.get("gross_weekly_salary"));
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCalculateNetWeeklySalary_ValidDateRange() {
        LocalDate startDate = LocalDate.of(2024, 1, 1); // Valid start date (Monday)
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Valid end date (Sunday)
        BigDecimal grossWeeklySalary = new BigDecimal("1000.00");
        BigDecimal sssDeduction = new BigDecimal("100.00");
        BigDecimal philhealthDeduction = new BigDecimal("50.00");
        BigDecimal pagibigDeduction = new BigDecimal("20.00");
        BigDecimal withholdingTax = new BigDecimal("30.00");

        // Mock the service methods to return predefined values
        when(salaryService.calculateGrossWeeklySalary("EMP001", startDate, endDate)).thenReturn(grossWeeklySalary);
        when(deductionsService.calculateWeeklySssDeduction("EMP001", startDate, endDate)).thenReturn(sssDeduction);
        when(deductionsService.calculateWeeklyPhilHealthDeduction("EMP001", startDate, endDate)).thenReturn(philhealthDeduction);
        when(deductionsService.calculateWeeklyPagIbigDeduction("EMP001", startDate, endDate)).thenReturn(pagibigDeduction);
        when(deductionsService.calculateWeeklyWithholdingTax("EMP001", startDate, endDate)).thenReturn(withholdingTax);

        // Calculate total deductions
        BigDecimal totalDeductions = sssDeduction.add(philhealthDeduction).add(pagibigDeduction).add(withholdingTax);
        BigDecimal netWeeklySalary = grossWeeklySalary.subtract(totalDeductions);

        // Call the controller method
        ResponseEntity<?> response = salaryController.calculateNetWeeklySalary(userDetails, "EMP001", startDate, endDate);

        // Assert that the response has an OK status and the correct net weekly salary along with deductions
        assertEquals(200, response.getStatusCodeValue()); // Expecting 200 OK
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> responseBody = (Map<String, BigDecimal>) response.getBody();
        assertEquals(grossWeeklySalary, responseBody.get("gross_weekly_salary"));
        assertEquals(sssDeduction, responseBody.get("weekly_sss_deduction"));
        assertEquals(philhealthDeduction, responseBody.get("weekly_philhealth_deduction"));
        assertEquals(pagibigDeduction, responseBody.get("weekly_pagibig_deduction"));
        assertEquals(withholdingTax, responseBody.get("weekly_withholding_tax"));
        assertEquals(totalDeductions, responseBody.get("total_deductions"));
        assertEquals(netWeeklySalary, responseBody.get("net_weekly_salary"));
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCalculateNetWeeklySalary_Unauthorized() {
        // Simulate an unauthorized user (null username)
        when(userDetails.getUsername()).thenReturn(null);

        LocalDate startDate = LocalDate.of(2024, 1, 1); // Valid start date (Monday)
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Valid end date (Sunday)

        // Call the controller method
        ResponseEntity<?> response = salaryController.calculateNetWeeklySalary(userDetails, "EMP001", startDate, endDate);

        // Assert that the response has an UNAUTHORIZED status
        assertEquals(401, response.getStatusCodeValue()); // Expecting 401 UNAUTHORIZED
    }
}
