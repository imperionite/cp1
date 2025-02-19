package com.imperionite.cp1.services;

import com.imperionite.cp1.entities.Attendance;
import com.imperionite.cp1.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    public void saveAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendancesByEmployeeNumberAndDateBetween(String employeeNumber, LocalDate startDate, LocalDate endDate){
        return attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNumber, startDate, endDate);
    }

    public List<Attendance> getAttendancesByDateBetween(LocalDate startDate, LocalDate endDate){
        return attendanceRepository.findByDateBetween(startDate, endDate);
    }

    public List<Attendance> getAttendancesByEmployeeNumber(String employeeNumber){
        return attendanceRepository.findByEmployeeNumber(employeeNumber);
    }

    // Updated weekly attendance retrieval with flexible date range
    public List<Attendance> getAttendancesForWeek(String employeeNumber, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findAttendancesForWeek(employeeNumber, startDate, endDate);
    }

    // New method to calculate total hours worked in a week
    public double calculateWeeklyHours(String employeeNumber, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = getAttendancesForWeek(employeeNumber, startDate, endDate);
        return attendances.stream()
                .mapToDouble(a -> java.time.Duration.between(a.getLogIn(), a.getLogOut()).toHours())
                .sum();
    }

    public Optional<Attendance> getAttendanceByEmployeeNumberAndDate(String employeeNumber, LocalDate date){
        return attendanceRepository.findByEmployeeNumberAndDate(employeeNumber, date);
    }

}