package com.imperionite.cp1.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {
    private String employeeNumber;
    private String date;
    private String logIn;
    private String logOut;
}