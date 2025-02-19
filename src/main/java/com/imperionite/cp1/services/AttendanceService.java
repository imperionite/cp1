//AttendanceService.java
package com.imperionite.cp1.services;

import com.imperionite.cp1.dtos.WeeklyCutoffDTO;
import com.imperionite.cp1.entities.Attendance;
import com.imperionite.cp1.repositories.AttendanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    @Autowired
    private AttendanceRepository attendanceRepository;


    /**
     * Saves a new attendance record.
     *
     * @param attendance The Attendance entity to be saved.
     */
    public void saveAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
        logger.debug("Attendance record saved: {}", attendance);
    }



    /**
     * Retrieves attendance records for a specific employee within a date range.
     *
     * @param employeeNumber The employee number.
     * @param startDate      The start date of the range (inclusive).
     * @param endDate        The end date of the range (inclusive).
     * @return A list of Attendance objects.
     */
    public List<Attendance> getAttendanceByEmployeeAndDateRange(String employeeNumber, LocalDate startDate,
                                                              LocalDate endDate) {
        logger.debug("Getting attendance for employee {} between {} and {}", employeeNumber, startDate, endDate);
        return attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNumber, startDate, endDate);
    }

    /**
     * Retrieves attendance records for all employees within a date range.
     *
     * @param startDate The start date of the range (inclusive).
     * @param endDate   The end date of the range (inclusive).
     * @return A list of Attendance objects.
     */
    public List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting all attendance between {} and {}", startDate, endDate);
        return attendanceRepository.findByDateBetween(startDate, endDate);
    }



    /**
     * Calculates the total work hours for a specific employee within a given week.
     *
     * @param employeeNumber The employee number.
     * @param startDate      The start date (Monday) of the week.
     * @param endDate        The end date (Sunday) of the week.
     * @return The total work hours as a double.
     * @throws IllegalArgumentException If the provided dates are not a Monday-Sunday week.
     */
    public double calculateWeeklyHours(String employeeNumber, LocalDate startDate, LocalDate endDate) {
        if (!startDate.getDayOfWeek().equals(DayOfWeek.MONDAY) || !endDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new IllegalArgumentException("Start date must be a Monday and end date must be a Sunday.");
        }

        List<Attendance> attendances = attendanceRepository.findAttendancesForWeek(employeeNumber, startDate, endDate);

        return attendances.stream()
                .mapToDouble(a -> ChronoUnit.MINUTES.between(a.getLogIn(), a.getLogOut()) / 60.0) // Convert to hours
                .sum();
    }



    /**
     * Retrieves the available weekly cut-offs (start and end dates).
     * This method queries the database for the minimum and maximum attendance dates
     * and generates a list of weekly cut-off periods between those dates.
     *
     * @return A list of {@link WeeklyCutoffDTO} objects, each representing a week
     *         with its start and end dates. Returns an empty list if no
     *         attendance records exist.
     */
    public List<WeeklyCutoffDTO> getWeeklyCutoffs() {
        LocalDate minDate = attendanceRepository.findMinDate();
        LocalDate maxDate = attendanceRepository.findMaxDate();

        if (minDate == null || maxDate == null) {
            return new ArrayList<>(); // Return empty list if no attendance records exist
        }

        List<WeeklyCutoffDTO> weeklyCutoffs = new ArrayList<>();
        LocalDate currentDate = minDate.with(DayOfWeek.MONDAY); // Start from the first Monday

        while (currentDate.isBefore(maxDate) || currentDate.isEqual(maxDate)) {
            LocalDate endDate = currentDate.with(DayOfWeek.SUNDAY);
            if (endDate.isAfter(maxDate)) {
                endDate = maxDate; // Adjust end date if it goes beyond maxDate
            }
            weeklyCutoffs.add(new WeeklyCutoffDTO(currentDate, endDate));
            currentDate = currentDate.plusWeeks(1);
        }

        return weeklyCutoffs;
    }

}