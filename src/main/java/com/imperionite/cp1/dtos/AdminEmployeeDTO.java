// DTO contains only the fields you specified: id, employeeNumber, lastName, firstName, birthday, and user

package com.imperionite.cp1.dtos;

import com.imperionite.cp1.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEmployeeDTO {
    private Long id;
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private LocalDate birthday;
    private User user;
}