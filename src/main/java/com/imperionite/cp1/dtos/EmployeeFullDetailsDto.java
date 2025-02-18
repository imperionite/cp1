package com.imperionite.cp1.dtos; // DTOs in a separate package

import com.imperionite.cp1.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*; // Lombok for cleaner code

// EmployeeFullDetails DTO (For Admin and Own Details)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFullDetailsDto {
    private Long id;
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private LocalDate birthday;
    private String address;
    private String phoneNumber;
    private String sss;
    private String philhealth;
    private String tin;
    private String pagibig;
    private String status;
    private String position;
    private String immediateSupervisor;
    private BigDecimal basicSalary;
    private BigDecimal riceSubsidy;
    private BigDecimal phoneAllowance;
    private BigDecimal clothingAllowance;
    private BigDecimal grossSemiMonthlyRate;
    private BigDecimal hourlyRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User user;
}
