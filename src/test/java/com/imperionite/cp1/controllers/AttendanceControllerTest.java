package com.imperionite.cp1.controllers;

import com.imperionite.cp1.dtos.AttendanceRequest;
import com.imperionite.cp1.dtos.WeeklyCutoffDTO;
import com.imperionite.cp1.entities.Attendance;
import com.imperionite.cp1.entities.Employee;
import com.imperionite.cp1.services.AttendanceService;
import com.imperionite.cp1.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AttendanceControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AttendanceController attendanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAttendance_Success() {
        AttendanceRequest request = new AttendanceRequest();
        request.setDate("01/01/2024");
        request.setLogIn("08:00");
        request.setLogOut("17:00");

        Employee employee = new Employee();
        employee.setEmployeeNumber("EMP001");
        employee.setLastName("Doe");
        employee.setFirstName("John");

        when(userDetails.getUsername()).thenReturn("EMP001");
        when(employeeService.getEmployeeByEmployeeNumber("EMP001")).thenReturn(Optional.of(employee));
        doNothing().when(attendanceService).saveAttendance(any(Attendance.class));

        ResponseEntity<?> response = attendanceController.createAttendance(request, userDetails);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateAttendance_EmployeeNotFound() {
        AttendanceRequest request = new AttendanceRequest();
        request.setDate("01/01/2024");
        request.setLogIn("08:00");
        request.setLogOut("17:00");

        when(userDetails.getUsername()).thenReturn("EMP001");
        when(employeeService.getEmployeeByEmployeeNumber("EMP001")).thenReturn(Optional.empty());

        ResponseEntity<?> response = attendanceController.createAttendance(request, userDetails);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetWeeklyCutoffs_Success() {
        List<WeeklyCutoffDTO> cutoffs = new ArrayList<>();
        cutoffs.add(new WeeklyCutoffDTO(LocalDate.now(), LocalDate.now().plusDays(6)));
        when(attendanceService.getWeeklyCutoffs()).thenReturn(cutoffs);

        ResponseEntity<List<WeeklyCutoffDTO>> response = attendanceController.getWeeklyCutoffs(userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAttendanceByEmployeeAndDateRange_Success() {
        List<Attendance> attendances = new ArrayList<>();
        attendances.add(new Attendance());
        when(attendanceService.getAttendanceByEmployeeAndDateRange(eq("EMP001"), any(LocalDate.class), any(LocalDate.class))).thenReturn(attendances);

        ResponseEntity<List<Attendance>> response = attendanceController.getAttendanceByEmployeeAndDateRange("EMP001", LocalDate.now(), LocalDate.now().plusDays(6));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAttendanceByDateRange_Success() {
        List<Attendance> attendances = new ArrayList<>();
        attendances.add(new Attendance());
        when(attendanceService.getAttendanceByDateRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(attendances);

        ResponseEntity<List<Attendance>> response = attendanceController.getAttendanceByDateRange(LocalDate.now(), LocalDate.now().plusDays(6));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testCalculateWeeklyHours_Success() {
        when(attendanceService.calculateWeeklyHours(eq("EMP001"), any(LocalDate.class), any(LocalDate.class))).thenReturn(BigDecimal.TEN);

        ResponseEntity<?> response = attendanceController.calculateWeeklyHours("EMP001", LocalDate.now(), LocalDate.now().plusDays(6));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.TEN, ((java.util.Map<String, BigDecimal>) response.getBody()).get("total_weekly_worked_hours"));
    }

    @Test
    void testCalculateWeeklyHours_InvalidDateRange() {
        when(attendanceService.calculateWeeklyHours(eq("EMP001"), any(LocalDate.class), any(LocalDate.class))).thenThrow(new IllegalArgumentException("Invalid date range"));

        ResponseEntity<?> response = attendanceController.calculateWeeklyHours("EMP001", LocalDate.now(), LocalDate.now().plusDays(6));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
