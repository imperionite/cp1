package com.imperionite.cp1.repositories;

import com.imperionite.cp1.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    Optional<Employee> findById(Long id);
}
