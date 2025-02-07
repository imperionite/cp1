package com.imperionite.cp1.services;

import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.repositories.EmployeeRepository;
import com.imperionite.cp1.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Employee createEmployee(Employee employee) {
        // If the employee object contains a user object with an id, fetch the user from the database
        Optional<User> userOpt = userRepository.findById(employee.getUser().getId());
        if (userOpt.isPresent()) {
            employee.setUser(userOpt.get());  // Set the User object on the Employee entity
            return employeeRepository.save(employee);  // Save the Employee with the associated User
        } else {
            throw new RuntimeException("User not found for ID: " + employee.getUser().getId());
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByEmployeeNumber(String employeeNumber) {
        return employeeRepository.findByEmployeeNumber(employeeNumber);
    }
}
