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
            User user4 = new User(null, "user4", encoder.encode(DEFAULT_PASSWORD), true, false, null, null);
            User user5 = new User(null, "user5", encoder.encode(DEFAULT_PASSWORD), true, false, null, null);

            // Save users
            userRepository.saveAll(Arrays.asList(admin, user0, user1, user2, user3, user4, user5));

            // Create initial employee data
            List<Employee> employees = Arrays.asList(
                    new Employee(
                            null, // id will be generated
                            "10001", // employeeNumber
                            "Garcia", // lastName
                            "Manuel III", // firstName
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
                            new BigDecimal("2000"), // phoneAllowance
                            new BigDecimal("1000"), // clothingAllowance
                            new BigDecimal("4500"), // // grossSemiMonthlyRate
                            new BigDecimal("535.71"), // hourlyRate
                            null, LocalDate.now(), // createdAt
                            user0 // associated user
                    ),
                    new Employee(
                            null,
                            "10002",
                            "Lim",
                            "Antonio",
                            LocalDate.of(1988, 6, 19),
                            "San Antonio De Padua 2, Block 1 Lot 8 and 2, Dasmarinas, Cavite",
                            "171-867-411",
                            "52-2061274-9",
                            "331735646338",
                            "683-102-776-000",
                            "663904995411",
                            "Regular",
                            "Chief Operating Officer",
                            "Garcia, Manuel III",
                            new BigDecimal("60000"),
                            new BigDecimal("1500"),
                            new BigDecimal("2000"),
                            new BigDecimal("1000"),
                            new BigDecimal("3000"),
                            new BigDecimal("357.14"),
                            null, LocalDate.now(),
                            user1),
                    new Employee(
                            null,
                            "10003",
                            "Aquino",
                            "Bianca Sofia",
                            LocalDate.of(1989, 8, 4),
                            "Rm. 402 4/F Jiao Building Timog Avenue Cor. Quezon Avenue 1100, Quezon City",
                            "966-889-370",
                            "30-8870406-2",
                            "177451189665",
                            "971-711-280-000",
                            "171519773969",
                            "Regular",
                            "Chief Finance Officer",
                            "Garcia, Manuel III",
                            new BigDecimal("60000"),
                            new BigDecimal("1500"),
                            new BigDecimal("2000"),
                            new BigDecimal("1000"),
                            new BigDecimal("3000"),
                            new BigDecimal("357.14"),
                            null, LocalDate.now(),
                            user2),

                    new Employee(
                            null,
                            "10004",
                            "Reyes",
                            "Isabella",
                            LocalDate.of(1994, 6, 16),
                            "460 Solanda Street Intramuros 1000, Manila",
                            "786-868-477",
                            "40-2511815-0",
                            "341911411254",
                            "876-809-437-000",
                            "416946776041",
                            "Regular",
                            "Chief Marketing Officer",
                            "Garcia, Manuel III",
                            new BigDecimal("60000"),
                            new BigDecimal("1500"),
                            new BigDecimal("2000"),
                            new BigDecimal("1000"),
                            new BigDecimal("3000"),
                            new BigDecimal("357.14"),
                            null, LocalDate.now(),
                            user3),
                    new Employee(
                            null,
                            "10005",
                            "Hernandez",
                            "Edward",
                            LocalDate.of(1989, 9, 23),
                            "National Highway, Gingoog,  Misamis Occidental",
                            "088-861-012",
                            "50-5577638-1",
                            "957436191812",
                            "031-702-374-000",
                            "952347222457",
                            "Regular",
                            "IT Operations and Systems",
                            "Lim, Antonio",
                            new BigDecimal("52670"),
                            new BigDecimal("1500"),
                            new BigDecimal("1000"),
                            new BigDecimal("1000"),
                            new BigDecimal("26335"),
                            new BigDecimal("313.51"),
                            null, LocalDate.now(),
                            user4),
                    new Employee(
                            null,
                            "10006",
                            "Villanueva",
                            "Andrea Mae",
                            LocalDate.of(1988, 2, 14),
                            "17/85 Stracke Via Suite 042, Poblacion, Las Piñas 4783 Dinagat Islands ",
                            "918-621-603",
                            "49-1632020-8",
                            "382189453145",
                            "317-674-022-000",
                            "441093369646",
                            "Regular",
                            "HR Manager",
                            "Lim, Antonio",
                            new BigDecimal("52670"),
                            new BigDecimal("1500"),
                            new BigDecimal("1000"),
                            new BigDecimal("1000"),
                            new BigDecimal("26335"),
                            new BigDecimal("313.51"),
                            null, LocalDate.now(),
                            user5)

            );

            // Save employees to the database
            employeeRepository.saveAll(employees);
            System.out.println("Database initialized with default employees.");
        } else {
            System.out.println("Database already contains employee data.");
        }
    }
}
