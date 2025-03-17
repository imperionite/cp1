package com.imperionite.cp1.services;

import com.imperionite.cp1.entities.Attendance;
import com.imperionite.cp1.dtos.WeeklyCutoffDTO;
import com.imperionite.cp1.repositories.AttendanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAttendance() {
        Attendance attendance = new Attendance();
        attendanceService.saveAttendance(attendance);
        verify(attendanceRepository, times(1)).save(attendance);
    }

    @Test
    void testGetAttendanceByEmployeeAndDateRange() {
        String employeeNumber = "EMP001";
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        List<Attendance> attendances = new ArrayList<>();
        attendances.add(new Attendance());
        when(attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNumber, startDate, endDate)).thenReturn(attendances);

        List<Attendance> result = attendanceService.getAttendanceByEmployeeAndDateRange(employeeNumber, startDate, endDate);
        assertEquals(attendances, result);
    }

    @Test
    void testGetAttendanceByDateRange() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        List<Attendance> attendances = new ArrayList<>();
        attendances.add(new Attendance());
        when(attendanceRepository.findByDateBetween(startDate, endDate)).thenReturn(attendances);

        List<Attendance> result = attendanceService.getAttendanceByDateRange(startDate, endDate);
        assertEquals(attendances, result);
    }

    @Test
    void testCalculateWeeklyHours_ValidWeek_NoLateLogin() {
        String employeeNumber = "EMP001";
        LocalDate startDate = LocalDate.of(2024, 1, 1); // Monday
        LocalDate endDate = LocalDate.of(2024, 1, 7); // Sunday

        List<Attendance> attendances = new ArrayList<>();
        Attendance attendance1 = new Attendance();
        attendance1.setLogIn(LocalTime.of(8, 0));
        attendance1.setLogOut(LocalTime.of(17, 0));
        attendances.add(attendance1);

        when(attendanceRepository.findAttendancesForWeek(employeeNumber, startDate, endDate)).thenReturn(attendances);

        BigDecimal expectedHours = new BigDecimal("9.00"); // 9 hours per day (with correct precision)
        BigDecimal actualHours = attendanceService.calculateWeeklyHours(employeeNumber, startDate, endDate);

        assertEquals(expectedHours, actualHours);
    }

    // @Test
    // void testCalculateWeeklyHours_ValidWeek_LateLogin() {
    //     String employeeNumber = "EMP001";
    //     LocalDate startDate = LocalDate.of(2024, 1, 1); // Monday
    //     LocalDate endDate = LocalDate.of(2024, 1, 7); // Sunday

    //     List<Attendance> attendances = new ArrayList<>();
    //     Attendance attendance1 = new Attendance();
    //     attendance1.setLogIn(LocalTime.of(8, 15)); // 15 minutes late
    //     attendance1.setLogOut(LocalTime.of(17, 0));
    //     attendances.add(attendance1);

    //     when(attendanceRepository.findAttendancesForWeek(employeeNumber, startDate, endDate)).thenReturn(attendances);

    //     BigDecimal expectedHours = new BigDecimal("8.75"); // 9 hours - 0.25 hours (15 minutes)
    //     BigDecimal actualHours = attendanceService.calculateWeeklyHours(employeeNumber, startDate, endDate);

    //     assertEquals(expectedHours, actualHours);
    // }

    @Test
    void testCalculateWeeklyHours_InvalidWeek_NotMondayToSunday() {
        String employeeNumber = "EMP001";
        LocalDate startDate = LocalDate.of(2024, 1, 2); // Tuesday
        LocalDate endDate = LocalDate.of(2024, 1, 8); // Monday

        assertThrows(IllegalArgumentException.class, () -> attendanceService.calculateWeeklyHours(employeeNumber, startDate, endDate));
    }

    // @Test
    // void testGetWeeklyCutoffs() {
    //     LocalDate minDate = LocalDate.of(2024, 1, 1);
    //     LocalDate maxDate = LocalDate.of(2024, 1, 31);
    //     when(attendanceRepository.findMinDate()).thenReturn(minDate);
    //     when(attendanceRepository.findMaxDate()).thenReturn(maxDate);

    //     List<WeeklyCutoffDTO> expectedCutoffs = new ArrayList<>();
    //     LocalDate startDate = minDate.with(java.time.DayOfWeek.MONDAY);
    //     LocalDate endDate;

    //     while (!startDate.isAfter(maxDate)) {
    //         endDate = startDate.with(java.time.DayOfWeek.SUNDAY);
    //         if (endDate.isAfter(maxDate)) {
    //             endDate = maxDate;
    //         }
    //         expectedCutoffs.add(new WeeklyCutoffDTO(startDate, endDate));
    //         startDate = startDate.plusWeeks(1);
    //     }

    //     List<WeeklyCutoffDTO> actualCutoffs = attendanceService.getWeeklyCutoffs();
    //     assertEquals(expectedCutoffs, actualCutoffs);
    // }

    @Test
    void testGetWeeklyCutoffs_NoAttendanceRecords() {
        when(attendanceRepository.findMinDate()).thenReturn(null);
        when(attendanceRepository.findMaxDate()).thenReturn(null);

        List<WeeklyCutoffDTO> actualCutoffs = attendanceService.getWeeklyCutoffs();
        assertTrue(actualCutoffs.isEmpty());
    }
}
