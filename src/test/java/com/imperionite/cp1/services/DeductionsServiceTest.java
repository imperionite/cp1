package com.imperionite.cp1.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imperionite.cp1.dtos.Contributions;
import com.imperionite.cp1.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeductionsServiceTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private DeductionsService deductionsService;

    @SuppressWarnings("unused")
    private Contributions contributions;
    private Employee employee;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() throws IOException {
        String json = "{\"sss\":[{\"salaryCap\":10000,\"contribution\":1000}],\"philhealth\":[{\"salaryCap\":10000,\"contribution\":500}]}";
        Resource resource = new ByteArrayResource(json.getBytes());
        when(resourceLoader.getResource(anyString())).thenReturn(resource);

        deductionsService.loadContributions();
        contributions = new ObjectMapper().readValue(json, Contributions.class);

        employee = new Employee();
        employee.setEmployeeNumber("123");
        employee.setBasicSalary(new BigDecimal("10000"));

        startDate = LocalDate.of(2024, Month.JANUARY, 1); // Monday
        endDate = LocalDate.of(2024, Month.JANUARY, 7);   // Sunday
    }

    @Test
    void loadContributions_success() throws IOException {
        String json = "{\"sss\":[{\"salaryCap\":10000,\"contribution\":1000}],\"philhealth\":[{\"salaryCap\":10000,\"contribution\":500}]}";
        Resource resource = new ByteArrayResource(json.getBytes());
        when(resourceLoader.getResource(anyString())).thenReturn(resource);

        deductionsService.loadContributions();

        assertNotNull(deductionsService.contributions);
    }

    @Test
    void calculateWeeklySssDeduction_employeeNotFound() {
        when(employeeService.getEmployeeByEmployeeNumber("123")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> deductionsService.calculateWeeklySssDeduction("123", startDate, endDate));
    }

    @Test
    void calculateWeeklySssDeduction_invalidDateRange() {
        LocalDate invalidEndDate = LocalDate.of(2024, Month.JANUARY, 6); // Saturday

        assertThrows(IllegalArgumentException.class, () -> deductionsService.calculateWeeklySssDeduction("123", startDate, invalidEndDate));
    }

    @Test
    void calculateWeeklyWithholdingTax_success() {
        when(employeeService.getEmployeeByEmployeeNumber("123")).thenReturn(Optional.of(employee));
        when(employeeService.getEmployeeByEmployeeNumber("123")).thenReturn(Optional.of(employee));
        BigDecimal result = deductionsService.calculateWeeklyWithholdingTax("123", startDate, endDate);
        assertNotNull(result);
    }

    @Test
    void calculateWeeklyWithholdingTax_employeeNotFound(){
        when(employeeService.getEmployeeByEmployeeNumber("123")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, ()-> deductionsService.calculateWeeklyWithholdingTax("123", startDate, endDate));
    }

    @Test
    void calculateWeeklyWithholdingTax_invalidDate(){
        LocalDate invalidEndDate = LocalDate.of(2024, Month.JANUARY, 6); // Saturday
        assertThrows(IllegalArgumentException.class, ()-> deductionsService.calculateWeeklyWithholdingTax("123", startDate, invalidEndDate));
    }
}