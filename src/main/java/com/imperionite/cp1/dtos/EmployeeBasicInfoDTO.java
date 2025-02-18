package com.imperionite.cp1.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBasicInfoDTO {
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private LocalDate birthday;

}
