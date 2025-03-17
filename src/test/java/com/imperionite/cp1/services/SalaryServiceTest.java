package com.imperionite.cp1.services;

import com.imperionite.cp1.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalaryServiceTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateGrossWeeklySalary_Success() {
        String employeeNumber = "EMP001";
        LocalDate startDate = LocalDate.of(2024, 1, 1); // Monday
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Sunday

        Employee employee = new Employee();
        employee.setEmployeeNumber(employeeNumber);
        employee.setHourlyRate(new BigDecimal("15.00"));

        BigDecimal workedHours = new BigDecimal("40"); // Assume 40 hours worked in the week

        when(employeeService.getEmployeeByEmployeeNumber(employeeNumber)).thenReturn(Optional.of(employee));
        when(attendanceService.calculateWeeklyHours(employeeNumber, startDate, endDate)).thenReturn(workedHours);

        BigDecimal expectedSalary = new BigDecimal("600.00"); // 40 hours * 15.00 hourly rate

        BigDecimal actualSalary = salaryService.calculateGrossWeeklySalary(employeeNumber, startDate, endDate);

        assertEquals(expectedSalary, actualSalary);
    }

    @Test
    void testCalculateGrossWeeklySalary_InvalidDateRange() {
        String employeeNumber = "EMP001";
        LocalDate startDate = LocalDate.of(2024, 1, 2); // Tuesday (invalid start date)
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Sunday

        assertThrows(IllegalArgumentException.class, () -> salaryService.calculateGrossWeeklySalary(employeeNumber, startDate, endDate));
    }

    @Test
    void testCalculateGrossWeeklySalary_EmployeeNotFound() {
        String employeeNumber = "EMP001";
        LocalDate startDate = LocalDate.of(2024, 1, 1); // Monday
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Sunday

        when(employeeService.getEmployeeByEmployeeNumber(employeeNumber)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> salaryService.calculateGrossWeeklySalary(employeeNumber, startDate, endDate));
    }

    @Test
    void testCalculateGrossWeeklySalary_SuccessWithDecimalSalary() {
        String employeeNumber = "EMP002";
        LocalDate startDate = LocalDate.of(2024, 1, 1); // Monday
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Sunday

        Employee employee = new Employee();
        employee.setEmployeeNumber(employeeNumber);
        employee.setHourlyRate(new BigDecimal("15.50"));

        BigDecimal workedHours = new BigDecimal("40"); // Assume 40 hours worked in the week

        when(employeeService.getEmployeeByEmployeeNumber(employeeNumber)).thenReturn(Optional.of(employee));
        when(attendanceService.calculateWeeklyHours(employeeNumber, startDate, endDate)).thenReturn(workedHours);

        BigDecimal expectedSalary = new BigDecimal("620.00"); // 40 hours * 15.50 hourly rate

        BigDecimal actualSalary = salaryService.calculateGrossWeeklySalary(employeeNumber, startDate, endDate);

        assertEquals(expectedSalary, actualSalary);
    }
}

