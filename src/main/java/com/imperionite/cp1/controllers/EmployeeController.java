package com.imperionite.cp1.controllers;

import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.services.EmployeeService;
import com.imperionite.cp1.services.UserService;
import com.imperionite.cp1.dtos.AdminEmployeeDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> optionalUser = userService.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();

            if (currentUser.getIsAdmin()) {
                Employee createdEmployee = employeeService.createEmployee(employee);
                return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> optionalUser = userService.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();

            if (currentUser.getIsAdmin()) {
                List<Employee> employees = employeeService.getAllEmployees();
                return new ResponseEntity<>(employees, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> optionalUser = userService.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();

            if (currentUser.getIsAdmin()) {
                Optional<Employee> employee = employeeService.getEmployeeById(id);
                return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<List<AdminEmployeeDTO>> getAllEmployeesForAdmin(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> optionalUser = userService.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();

            if (currentUser.getIsAdmin()) {
                List<Employee> employees = employeeService.getAllEmployees();
                List<AdminEmployeeDTO> employeeDTOs = employees.stream()
                        .map(employee -> new AdminEmployeeDTO(
                                employee.getId(),
                                employee.getEmployeeNumber(),
                                employee.getLastName(),
                                employee.getFirstName(),
                                employee.getBirthday(),
                                employee.getUser()
                        ))
                        .collect(Collectors.toList());
                return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/employeeNumber/{employeeNumber}")
    public ResponseEntity<Employee> getEmployeeByEmployeeNumber(@PathVariable String employeeNumber) {
        Optional<Employee> employee = employeeService.getEmployeeByEmployeeNumber(employeeNumber);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
