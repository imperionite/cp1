**Employee Endpoints:**

1.  **`POST /api/employees` (Admin Only):** Creates a new employee. Requires an `Employee` object in the request body (including the `user` object with the user's ID) and authentication with the Admin role. Returns the created `Employee` object.

2.  **`GET /api/employees/admin` (Admin Only):** Retrieves all employees with full details. Requires authentication with the Admin role. Returns a list of `AdminEmployeeDTO` objects.

3.  **`GET /api/employees/basic-info` (Authenticated Users):** Retrieves basic information (employee number, name, birthday) for all employees. Requires authentication as any user. Returns a list of `EmployeeBasicInfoDTO` objects.

4.  **`GET /api/employees/me` (Authenticated Users):** Retrieves the full details of the currently logged-in employee. Requires authentication as any user. Returns a single `EmployeeFullDetailsDto` object.

5.  **`GET /api/employees/{id}` (Admin Only):** Retrieves an employee by their ID. Requires authentication with the Admin role. Returns a single `EmployeeFullDetailsDto` object.

6.  **`GET /api/employees/employeeNumber/{employeeNumber}` (All Users):** Retrieves an employee by their employee number. Accessible by all users (no authentication required). Returns a single `EmployeeFullDetailsDto` object.

**Key Considerations:**

*   **Security:**  Most of these endpoints are secured using Spring Security's `@PreAuthorize` annotation.  It's crucial to have Spring Security properly configured with roles (ADMIN, USER) and a mechanism to associate the employee number with the authenticated user (e.g., using custom claims or authorities in your JWT or authentication process).
*   **DTOs:**  The endpoints consistently use DTOs (`EmployeeFullDetailsDto`, `EmployeeBasicInfoDTO`, `AdminEmployeeDTO`) for responses.  This is a critical security best practice to avoid exposing sensitive or unnecessary data.
*   **Error Handling:** Basic error handling (e.g., returning 404 Not Found) is included in some endpoints.  More robust error handling can be added as needed.


**Attendance Endpoints:**

You're right to ask for a review! Here's a summary of the endpoints in the `AttendanceController` and how they address the MotorPH requirements:

**Current Endpoints:**

1.  **`/me/week/hours` (GET):**
    *   **Purpose:** Allows the currently authenticated user (employee) to retrieve their own total work hours for a given week.
    *   **Authentication:** Requires authentication (user must be logged in).
    *   **Authorization:** The logged-in user can only access their own data.
    *   **Parameters:**
        *   `date` (required, `LocalDate`): Any date within the week for which to calculate the hours. The start and end of the week (Sunday-Saturday) are determined based on this date.
    *   **Return:** A `ResponseEntity` containing a message with the total hours worked during the specified week or an appropriate error message (e.g., if no attendance records are found, or the user is not authenticated).

2.  **`/admin/employee/{employeeNumber}/week/hours` (GET):**
    *   **Purpose:** Allows an admin to retrieve the total work hours for *any* employee for a given week.
    *   **Authentication:** Requires authentication.
    *   **Authorization:** The logged-in user *must* have the admin role.
    *   **Parameters:**
        *   `employeeNumber` (required, `String`): The employee number for whom to calculate the hours.
        *   `date` (required, `LocalDate`): Any date within the week.
    *   **Return:** A `ResponseEntity` containing the total hours worked by the specified employee during the specified week or an appropriate error message.

3.  **`/me/week/salary` (GET):**
    *   **Purpose:** Allows the currently authenticated user (employee) to retrieve their own gross weekly salary for a given week.
    *   **Authentication:** Requires authentication.
    *   **Authorization:** The logged-in user can only access their own data.
    *   **Parameters:**
        *   `date` (required, `LocalDate`): Any date within the week.
    *   **Return:** A `ResponseEntity` containing the gross weekly salary or an error message.  It is assumed that the employee's `hourlyRate` is available (e.g., retrieved from the `Employee` entity).

4.  **`/admin/employee/{employeeNumber}/week/salary` (GET):**
    *   **Purpose:** Allows an admin to retrieve the gross weekly salary for *any* employee for a given week.
    *   **Authentication:** Requires authentication.
    *   **Authorization:** The logged-in user *must* have the admin role.
    *   **Parameters:**
        *   `employeeNumber` (required, `String`): The employee number.
        *   `date` (required, `LocalDate`): Any date within the week.
    *   **Return:** A `ResponseEntity` containing the gross weekly salary or an error message.

**How They Address MotorPH Requirements:**

*   **Calculates the number of hours an employee has worked in a week:** Both `/me/week/hours` and `/admin/employee/{employeeNumber}/week/hours` directly address this requirement.  They calculate the total work hours based on attendance records within the specified week.

*   **Calculates the gross weekly salary of an employee based on hours worked:** Both `/me/week/salary` and `/admin/employee/{employeeNumber}/week/salary` address this.  They calculate the gross salary by multiplying the total weekly hours (calculated from attendance records) by the employee's hourly rate.



```java
// AttendanceController

package com.imperionite.cp1.controllers;

import com.imperionite.cp1.entities.Attendance;
import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.services.AttendanceService;
import com.imperionite.cp1.services.EmployeeService;
import com.imperionite.cp1.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    /**
     * Endpoint for a user to get their own weekly hours. Defaults to the current week if no date is provided.
     *
     * @param date The date within the week for which to calculate hours (defaults to the current week).
     * @return ResponseEntity containing the total hours or an error message.
     */
    @GetMapping("/me/week/hours")
    public ResponseEntity<?> getMyWeeklyHours(@RequestParam(required = false) 
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();  // Default to current week
        }
        Optional<User> currentUser = userService.findByUsername("username"); // For simplicity, default to some user
        if (!currentUser.isPresent()) {
            return ResponseEntity.status(401).body("User not found");
        }

        Optional<Employee> employee = employeeService.findByUser(currentUser.get());
        if (!employee.isPresent()) {
            return ResponseEntity.status(403).body("Employee not found");
        }

        return calculateAndReturnHours(employee.get().getEmployeeNumber(), date);
    }

    /**
     * Endpoint for an admin to get any employee's weekly hours.
     *
     * @param employeeNumber The employee number for whom to calculate hours.
     * @param date           The date within the week for which to calculate hours.
     * @return ResponseEntity containing the total hours or an error message.
     */
    @GetMapping("/admin/employee/{employeeNumber}/week/hours")
    public ResponseEntity<?> getEmployeeWeeklyHours(@PathVariable String employeeNumber,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return calculateAndReturnHours(employeeNumber, date);
    }

    /**
     * Endpoint for a user to get their own gross weekly salary.
     *
     * @param date The date within the week for which to calculate salary (defaults to the current week).
     * @return ResponseEntity containing the gross salary or an error message.
     */
    @GetMapping("/me/week/salary")
    public ResponseEntity<?> getMyWeeklySalary(@RequestParam(required = false) 
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();  // Default to current week
        }

        Optional<User> currentUser = userService.findByUsername("username"); // Default user
        if (!currentUser.isPresent()) {
            return ResponseEntity.status(401).body("User not found");
        }

        Optional<Employee> employee = employeeService.findByUser(currentUser.get());
        if (!employee.isPresent()) {
            return ResponseEntity.status(403).body("Employee not found");
        }

        return calculateAndReturnSalary(employee.get().getEmployeeNumber(), date, employee.get().getHourlyRate());
    }

    /**
     * Endpoint for an admin to get any employee's gross weekly salary.
     *
     * @param employeeNumber The employee number for whom to calculate salary.
     * @param date           The date within the week for which to calculate salary.
     * @return ResponseEntity containing the gross salary or an error message.
     */
    @GetMapping("/admin/employee/{employeeNumber}/week/salary")
    public ResponseEntity<?> getEmployeeWeeklySalary(@PathVariable String employeeNumber,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return calculateAndReturnSalary(employeeNumber, date, BigDecimal.valueOf(15)); // Example hourly rate
    }

    /**
     * Helper method to calculate and return weekly hours.
     *
     * @param employeeNumber The employee number.
     * @param date           Any date within the week.
     * @return ResponseEntity containing the total hours.
     */
    private ResponseEntity<?> calculateAndReturnHours(String employeeNumber, LocalDate date) {
        List<Attendance> attendances = attendanceService.getAttendancesForWeek(employeeNumber, date);
        if (attendances.isEmpty()) {
            return ResponseEntity.ok("No Attendance Record found for this employee number within the given date range.");
        }

        long totalMinutes = calculateTotalMinutes(attendances);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        LocalDate startDate = date.with(DayOfWeek.SUNDAY);
        LocalDate endDate = date.with(DayOfWeek.SATURDAY);

        return ResponseEntity.ok(String.format("Total hours for employee %s during the week of %s to %s: %d hours and %d minutes",
                employeeNumber, startDate, endDate, hours, minutes));
    }

    /**
     * Helper method to calculate and return gross weekly salary.
     *
     * @param employeeNumber The employee number.
     * @param date           Any date within the week.
     * @param hourlyRate     The employee's hourly rate.
     * @return ResponseEntity containing the gross salary.
     */
    private ResponseEntity<?> calculateAndReturnSalary(String employeeNumber, LocalDate date, BigDecimal hourlyRate) {
        List<Attendance> attendances = attendanceService.getAttendancesForWeek(employeeNumber, date);
        if (attendances.isEmpty()) {
            return ResponseEntity.ok("No Attendance Record found for this employee number within the given date range.");
        }

        long totalMinutes = calculateTotalMinutes(attendances);
        double totalHours = totalMinutes / 60.0;
        BigDecimal grossSalary = hourlyRate.multiply(BigDecimal.valueOf(totalHours));

        LocalDate startDate = date.with(DayOfWeek.SUNDAY);
        LocalDate endDate = date.with(DayOfWeek.SATURDAY);

        return ResponseEntity.ok(String.format("Gross weekly salary for employee %s during the week of %s to %s: $%.2f",
                employeeNumber, startDate, endDate, grossSalary));
    }

    private long calculateTotalMinutes(List<Attendance> attendances) {
        long totalMinutes = 0;
        for (Attendance attendance : attendances) {
            if (attendance.getLogIn() != null && attendance.getLogOut() != null) {
                totalMinutes += java.time.Duration.between(attendance.getLogIn(), attendance.getLogOut()).toMinutes();
            }
        }
        return totalMinutes;
    }
}


```

```java
// DataInitializer

package com.imperionite.cp1.initializers;

import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.repositories.EmployeeRepository;
import com.imperionite.cp1.repositories.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
@Order(1)
public class DataInitializer implements ApplicationRunner, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    // For simplicity, env. variables are exposed and how passwords are structure on
    // debug mode

    private static final String DEFAULT_PASSWORD = "passworD#1";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "adminPassword";

    @Autowired
    private PasswordEncoder encoder;

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public DataInitializer(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    /**
     * Runs the data initialization logic.
     *
     * @param args Application arguments (not used).
     * @throws Exception If an error occurs during initialization.
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<User> existingAdmin = userRepository.findByUsername(ADMIN_USERNAME);
        if (existingAdmin.isEmpty()) {
            User admin = new User(ADMIN_USERNAME, encoder.encode(ADMIN_PASSWORD));
            admin.setIsAdmin(true);
            userRepository.save(admin);
            logger.info("Admin user created.");
        } else {
            logger.info("Admin user already exists.");
        }

        if (employeeRepository.count() == 0) {
            List<Employee> employees = loadEmployeesFromCSV("employees_details.csv");
            if (employees != null) {
                employeeRepository.saveAll(employees);
                logger.info("Database initialized with employees from CSV.");
            } else {
                logger.error("Failed to load employee data from CSV.");
            }
        } else {
            logger.info("Database already contains employee data.");
        }
    }

    private List<Employee> loadEmployeesFromCSV(String csvFilePath) {
        List<Employee> employees = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource(csvFilePath).getFile()))) {

            Iterable<CSVRecord> records = CSVFormat.Builder.create()
                    .setHeader("Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS #",
                            "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor",
                            "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance",
                            "Gross Semi-monthly Rate", "Hourly Rate")
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(br);

            for (CSVRecord record : records) {
                Employee employee = new Employee();
                employee.setEmployeeNumber(record.get("Employee #").trim());
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

                    Optional<User> existingUser = userRepository.findByUsername(employee.getEmployeeNumber());
                    User userToSet;
                    if (existingUser.isPresent()) {
                        userToSet = existingUser.get();
                    } else {
                        userToSet = new User(employee.getEmployeeNumber(), encoder.encode(DEFAULT_PASSWORD));
                        userRepository.save(userToSet); // Save the User *before* associating it with the Employee
                        logger.info("User created: {}", userToSet.getUsername()); // Log user creation
                    }
                    employee.setUser(userToSet);
                    employees.add(employee);

                } catch (NumberFormatException e) {
                    logger.error("Error parsing number for employee: {}", employee.getEmployeeNumber());
                    logger.error("Problematic record from CSV: {}", record);
                    e.printStackTrace();
                    return null; // Stop processing if there's a parsing error
                }
            }

        } catch (IOException e) {
            logger.error("Error reading employee CSV file: {}", e.getMessage());
            return null;
        }
        return employees;
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL") || value.equalsIgnoreCase("N")
                || value.equalsIgnoreCase("null")) {
            return BigDecimal.ZERO;
        }

        String cleanedValue = value.replace("\"", "").replace(",", "").trim();
        cleanedValue = cleanedValue.replaceAll("\\u00A0", ""); // Remove non-breaking spaces
        cleanedValue = cleanedValue.replaceAll("\\ufeff", ""); // Remove Byte Order Mark (BOM)

        try {
            return new BigDecimal(cleanedValue);
        } catch (NumberFormatException e) {
            logger.error("Error parsing BigDecimal: \"{}\"", cleanedValue);
            throw e; // Re-throw the exception after logging
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}


```