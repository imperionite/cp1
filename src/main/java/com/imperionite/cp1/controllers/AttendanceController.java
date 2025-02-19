package com.imperionite.cp1.controllers;

import com.imperionite.cp1.dtos.AttendanceRequest;
import com.imperionite.cp1.entities.Attendance;
import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.repositories.AttendanceRepository;
import com.imperionite.cp1.repositories.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping
    public ResponseEntity<?> createAttendance(@RequestBody AttendanceRequest attendanceRequest) {
        try {
            // 1. Find the employee
            Optional<Employee> employee = employeeRepository
                    .findByEmployeeNumber(attendanceRequest.getEmployeeNumber());
            if (employee.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee not found.");
            }

            // 2. Parse and validate date and time
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // Allow MM/dd/yyyy
            LocalDate date = LocalDate.parse(attendanceRequest.getDate(), dateFormatter);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime logIn = LocalTime.parse(attendanceRequest.getLogIn(), timeFormatter);
            LocalTime logOut = LocalTime.parse(attendanceRequest.getLogOut(), timeFormatter);

            // 3. Create Attendance record
            Attendance attendance = new Attendance();
            attendance.setEmployeeNumber(employee.get().getEmployeeNumber());
            attendance.setLastName(employee.get().getLastName());
            attendance.setFirstName(employee.get().getFirstName());
            attendance.setDate(date);
            attendance.setLogIn(logIn);
            attendance.setLogOut(logOut);
            attendanceRepository.save(attendance);

            return ResponseEntity.status(HttpStatus.CREATED).body("Attendance record created.");

        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date or time format. Use MM/dd/yyyy for date and HH:mm for time.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating attendance record: " + e.getMessage());
        }
    }

}