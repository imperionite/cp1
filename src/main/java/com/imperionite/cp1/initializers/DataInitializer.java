package com.imperionite.cp1.initializers;

import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.repositories.EmployeeRepository;
import com.imperionite.cp1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final String DEFAULT_PASSWORD = "passworD#1";

    @Autowired
    private PasswordEncoder encoder;

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public DataInitializer(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // Check if employee data exists
        if (employeeRepository.count() == 0) {
            // Load initial users
            User admin = new User(null, "admin", encoder.encode("adminPassword"), true, true, null, null);
            User user0 = new User(null, "user0", encoder.encode(DEFAULT_PASSWORD), true, false, null, null);
            User user1 = new User(null, "user1", encoder.encode(DEFAULT_PASSWORD), true, false, null, null);
            User user2 = new User(null, "user2", encoder.encode(DEFAULT_PASSWORD), true, false, null, null);
            User user3 = new User(null, "user3", encoder.encode(DEFAULT_PASSWORD), true, false, null, null);
            
            // Save users
            userRepository.saveAll(Arrays.asList(admin, user0, user1, user2, user3));

            // Create initial employee data
            List<Employee> employees = Arrays.asList(
                new Employee(
                    null,  // id will be generated
                    "10001", // employeeNumber
                    "Garcia", // lastName
                    "Manuel III",  // firstName
                    LocalDate.of(1983, 10, 11), // birthday
                    "Valero Carpark Building Valero Street 1227, Makati City", // address
                    "966-860-270", // phoneNumber
                    "44-4506057-3", // sss
                    "820126853951", // philhealth
                    "442-605-657-000", // tin
                    "691295330870", // pagibig
                    "Regular", // status
                    "Chief Executive Officer", // position
                    "N/A", // immediateSupervisor
                    new BigDecimal("90000"), // basicSalary
                    new BigDecimal("1500"), // riceSubsidy
                    new BigDecimal("2000"),  // phoneAllowance
                    new BigDecimal("1000"),  // clothingAllowance
                    new BigDecimal("4500"),  // // grossSemiMonthlyRate
                    new BigDecimal("535.71"),  // hourlyRate
                    null, LocalDate.now(),  // createdAt
                    user1  // associated user
                ),
                new Employee(
                    null,  // id will be generated
                    "EMP002",  // employeeNumber
                    "Smith",  // lastName
                    "Jane",  // firstName
                    LocalDate.of(1985, 5, 10),  // birthday
                    "456 Oak St, Springfield, IL",  // address
                    "987-654-3210",  // phoneNumber
                    "234-56-7890",  // sss
                    "PH987654321",  // philhealth
                    "TIN9876543",  // tin
                    "PAGIBIG987654",  // pagibig
                    "Active",  // status
                    "Product Manager",  // position
                    "John Doe",  // immediateSupervisor
                    new BigDecimal("75000.00"),  // basicSalary
                    new BigDecimal("1200.00"),  // phoneAllowance
                    new BigDecimal("600.00"),  // clothingAllowance
                    new BigDecimal("35000.00"),  // grossSemiMonthlyRate
                    new BigDecimal("180.00"),  // hourlyRate
                    null, LocalDate.now(),  // createdAt
                    LocalDate.now(),  // updatedAt
                    user2  // associated user
                )
            );

            // Save employees to the database
            employeeRepository.saveAll(employees);
            System.out.println("Database initialized with default employees.");
        } else {
            System.out.println("Database already contains employee data.");
        }
    }
}
