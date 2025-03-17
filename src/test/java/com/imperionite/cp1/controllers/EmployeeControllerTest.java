package com.imperionite.cp1.controllers;

import com.imperionite.cp1.dtos.AdminEmployeeDTO;
import com.imperionite.cp1.dtos.EmployeeBasicInfoDTO;
import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.services.EmployeeService;
import com.imperionite.cp1.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee_Admin_Success() {
        User adminUser = new User();
        adminUser.setIsAdmin(true);
        Employee employee = new Employee();
        when(userDetails.getUsername()).thenReturn("admin");
        when(userService.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(employeeService.createEmployee(employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.createEmployee(employee, userDetails);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateEmployee_NotAdmin() {
        User normalUser = new User();
        normalUser.setIsAdmin(false);
        when(userDetails.getUsername()).thenReturn("user");
        when(userService.findByUsername("user")).thenReturn(Optional.of(normalUser));

        ResponseEntity<Employee> response = employeeController.createEmployee(new Employee(), userDetails);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testGetAllEmployees_Admin() {
        User adminUser = new User();
        adminUser.setIsAdmin(true);
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        when(userDetails.getUsername()).thenReturn("admin");
        when(userService.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees(userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetEmployeeById_Admin_Found() {
        User adminUser = new User();
        adminUser.setIsAdmin(true);
        Employee employee = new Employee();
        employee.setId(1L);
        when(userDetails.getUsername()).thenReturn("admin");
        when(userService.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));

        ResponseEntity<?> response = employeeController.getEmployeeById(1L, userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetEmployeeById_Admin_NotFound() {
        User adminUser = new User();
        adminUser.setIsAdmin(true);
        when(userDetails.getUsername()).thenReturn("admin");
        when(userService.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = employeeController.getEmployeeById(1L, userDetails);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllEmployeesForAdmin_Admin() {
        User adminUser = new User();
        adminUser.setIsAdmin(true);
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeNumber("EMP001");
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setBirthday(LocalDate.now());
        employee.setUser(new User());
        employees.add(employee);

        when(userDetails.getUsername()).thenReturn("admin");
        when(userService.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<AdminEmployeeDTO>> response = employeeController.getAllEmployeesForAdmin(userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("EMP001", response.getBody().get(0).getEmployeeNumber());
    }

    @Test
    void testGetEmployeeByEmployeeNumber_Admin_Found() {
        User adminUser = new User();
        adminUser.setIsAdmin(true);
        Employee employee = new Employee();
        employee.setEmployeeNumber("EMP001");
        when(userDetails.getUsername()).thenReturn("admin");
        when(userService.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(employeeService.getEmployeeByEmployeeNumber("EMP001")).thenReturn(Optional.of(employee));

        ResponseEntity<Employee> response = employeeController.getEmployeeByEmployeeNumber("EMP001", userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("EMP001", response.getBody().getEmployeeNumber());
    }

    @Test
    void testGetBasicInfoByEmployeeNumber_Found() {
        Employee employee = new Employee();
        employee.setEmployeeNumber("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setBirthday(LocalDate.now());
        User user = new User();

        when(userDetails.getUsername()).thenReturn("user");
        when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        when(employeeService.getEmployeeByEmployeeNumber("EMP001")).thenReturn(Optional.of(employee));

        ResponseEntity<EmployeeBasicInfoDTO> response = employeeController.getBasicInfoByEmployeeNumber("EMP001", userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("EMP001", response.getBody().getEmployeeNumber());
    }

    @Test
    void testGetMyDetails_Found() {
        User user = new User();
        Employee employee = new Employee();
        when(userDetails.getUsername()).thenReturn("user");
        when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        when(employeeService.findByUser(user)).thenReturn(Optional.of(employee));

        ResponseEntity<?> response = employeeController.getMyDetails(userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetMyDetails_NotFound() {
        User user = new User();
        when(userDetails.getUsername()).thenReturn("user");
        when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        when(employeeService.findByUser(user)).thenReturn(Optional.empty());

        ResponseEntity<?> response = employeeController.getMyDetails(userDetails);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testGetAllEmployeesBasicInfo(){
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setEmployeeNumber("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setBirthday(LocalDate.now());
        employees.add(employee);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<EmployeeBasicInfoDTO>> response = employeeController.getAllEmployeesBasicInfo();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("EMP001", response.getBody().get(0).getEmployeeNumber());
    }
}