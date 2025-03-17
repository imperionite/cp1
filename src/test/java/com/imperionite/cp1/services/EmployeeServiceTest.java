package com.imperionite.cp1.services;

import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.repositories.EmployeeRepository;
import com.imperionite.cp1.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee_Success() {
        User user = new User();
        user.setId(1L);
        Employee employee = new Employee();
        employee.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee createdEmployee = employeeService.createEmployee(employee);
        assertEquals(employee, createdEmployee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testCreateEmployee_UserNotFound() {
        User user = new User();
        user.setId(1L);
        Employee employee = new Employee();
        employee.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> employeeService.createEmployee(employee));
        verify(employeeRepository, never()).save(employee);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        employees.add(new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeById_Found() {
        Employee employee = new Employee();
        employee.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById(1L);
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByUser_Found() {
        User user = new User();
        Employee employee = new Employee();
        employee.setUser(user);
        when(employeeRepository.findByUser(user)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.findByUser(user);
        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
        verify(employeeRepository, times(1)).findByUser(user);
    }

    @Test
    void testFindByUser_NotFound() {
        User user = new User();
        when(employeeRepository.findByUser(user)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.findByUser(user);
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetEmployeeByEmployeeNumber_Found() {
        Employee employee = new Employee();
        employee.setEmployeeNumber("EMP001");
        when(employeeRepository.findByEmployeeNumber("EMP001")).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeByEmployeeNumber("EMP001");
        assertTrue(result.isPresent());
        assertEquals("EMP001", result.get().getEmployeeNumber());
        verify(employeeRepository, times(1)).findByEmployeeNumber("EMP001");
    }

    @Test
    void testGetEmployeeByEmployeeNumber_NotFound() {
        when(employeeRepository.findByEmployeeNumber("EMP001")).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeByEmployeeNumber("EMP001");
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findByEmployeeNumber("EMP001");
    }
}
