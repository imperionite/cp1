package com.imperionite.cp1.initializers;

import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.repositories.EmployeeRepository;
import com.imperionite.cp1.repositories.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final String DEFAULT_PASSWORD = "passworD#1"; // Or a more secure default
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "adminPassword"; // Change this in production

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
        Optional<User> existingAdmin = userRepository.findByUsername(ADMIN_USERNAME);
        if (existingAdmin.isEmpty()) {
            User admin = new User(ADMIN_USERNAME, encoder.encode(ADMIN_PASSWORD));
            admin.setIsAdmin(true);
            userRepository.save(admin);
        }

        createUsersFromCSV("employees_details.csv"); // Create users first

        List<Employee> employees = loadEmployeesFromCSV("employees_details.csv");

        if (employees != null && !employees.isEmpty()) { // Check if employees list is not null and not empty
            employeeRepository.saveAll(employees);
            System.out.println("Database initialized with employees from CSV.");

            updateUserAssociations(employees); // Update user associations using IDs

        } else if (employees == null) {
            System.err.println("Failed to load employee data from CSV.");
        } else {
            System.out.println("No new employees to add from CSV."); // Handle empty CSV case
        }
    }

    private void createUsersFromCSV(String csvFilePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(csvFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {
            Iterable<CSVRecord> records = CSVFormat.Builder.create()
                    .setHeader("Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate")
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(br);

            for (CSVRecord record : records) {
                String employeeNumber = record.get("Employee #").trim();
                Optional<User> existingUser = userRepository.findByUsername(employeeNumber);
                if (existingUser.isEmpty()) {
                    User newUser = new User(employeeNumber, encoder.encode(DEFAULT_PASSWORD));
                    userRepository.save(newUser);
                }
            }
        }
    }


    private List<Employee> loadEmployeesFromCSV(String csvFilePath) {
        List<Employee> employees = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {

                Iterable<CSVRecord> records = CSVFormat.Builder.create()
                        .setHeader("Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate")
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(br);

                for (CSVRecord record : records) {
                    String employeeNumber = record.get("Employee #").trim();

                    if (employeeRepository.findByEmployeeNumber(employeeNumber).isPresent()) {
                        System.out.println("Skipping duplicate employee number: " + employeeNumber);
                        continue; // Skip to the next record
                    }

                    Employee employee = new Employee();
                    employee.setEmployeeNumber(employeeNumber);
                    employee.setLastName(record.get("Last Name").trim());
                    employee.setFirstName(record.get("First Name").trim());
                    employee.setBirthday(LocalDate.parse(record.get("Birthday").trim(), dateFormatter));
                    employee.setAddress(record.get("Address").trim());
                    employee.setPhoneNumber(record.get("Phone Number").trim());
                    employee.setSss(record.get("SSS #").trim());
                    employee.setPhilhealth(record.get("Philhealth #").trim());
                    employee.setTin(record.get("TIN #").trim());
                    employee.setPagibig(record.get("Pag-ibig #").trim());
                    employee.setStatus(record.get("Status").trim());
                    employee.setPosition(record.get("Position").trim());
                    employee.setImmediateSupervisor(record.get("Immediate Supervisor").trim());

                    try {
                        employee.setBasicSalary(parseBigDecimal(record.get("Basic Salary")));
                        employee.setRiceSubsidy(parseBigDecimal(record.get("Rice Subsidy")));
                        employee.setPhoneAllowance(parseBigDecimal(record.get("Phone Allowance")));
                        employee.setClothingAllowance(parseBigDecimal(record.get("Clothing Allowance")));
                        employee.setGrossSemiMonthlyRate(parseBigDecimal(record.get("Gross Semi-monthly Rate")));
                        employee.setHourlyRate(parseBigDecimal(record.get("Hourly Rate")));

                        employees.add(employee); // Add employee to the list *before* setting user

                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing number for employee: " + employee.getEmployeeNumber());
                        System.err.println("Problematic record from CSV: " + record);
                        continue; // Skip to the next CSV record
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return employees;
    }

    private void updateUserAssociations(List<Employee> employees) {
        for (Employee employee : employees) {
            String username = employee.getEmployeeNumber();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                employee.setUser(user.get());
            } else {
                System.err.println("User not found for employee: " + username);
            }
        }
        employeeRepository.saveAll(employees);
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL") || value.equalsIgnoreCase("N") || value.equalsIgnoreCase("null")) {
            return BigDecimal.ZERO;
        }

        String cleanedValue = value.replace("\"", "").replace(",", "").trim();
        cleanedValue = cleanedValue.replaceAll("\\u00A0", ""); // Remove non-breaking spaces
        cleanedValue = cleanedValue.replaceAll("\\ufeff", ""); // Remove Byte Order Mark (BOM)

        System.out.println("Converting to BigDecimal: \"" + cleanedValue + "\""); // For debugging

        try {
            return new BigDecimal(cleanedValue);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing BigDecimal: \"" + cleanedValue + "\"");
            throw e; // Re-throw the exception after logging
        }
    }
}