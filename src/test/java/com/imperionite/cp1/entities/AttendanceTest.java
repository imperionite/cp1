package com.imperionite.cp1.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceTest {

    @Test
    void testAttendanceConstructorAndGetters() {
        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime logIn = LocalTime.of(8, 0);
        LocalTime logOut = LocalTime.of(17, 0);

        Attendance attendance = new Attendance(1L, "EMP001", "Doe", "John", date, logIn, logOut);

        assertEquals(1L, attendance.getId());
        assertEquals("EMP001", attendance.getEmployeeNumber());
        assertEquals("Doe", attendance.getLastName());
        assertEquals("John", attendance.getFirstName());
        assertEquals(date, attendance.getDate());
        assertEquals(logIn, attendance.getLogIn());
        assertEquals(logOut, attendance.getLogOut());
    }

    @Test
    void testAttendanceSetters() {
        Attendance attendance = new Attendance();
        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime logIn = LocalTime.of(8, 0);
        LocalTime logOut = LocalTime.of(17, 0);

        attendance.setId(1L);
        attendance.setEmployeeNumber("EMP001");
        attendance.setLastName("Doe");
        attendance.setFirstName("John");
        attendance.setDate(date);
        attendance.setLogIn(logIn);
        attendance.setLogOut(logOut);

        assertEquals(1L, attendance.getId());
        assertEquals("EMP001", attendance.getEmployeeNumber());
        assertEquals("Doe", attendance.getLastName());
        assertEquals("John", attendance.getFirstName());
        assertEquals(date, attendance.getDate());
        assertEquals(logIn, attendance.getLogIn());
        assertEquals(logOut, attendance.getLogOut());
    }

    @Test
    void testToString() {
        Attendance attendance = new Attendance(1L, "EMP001", "Doe", "John", LocalDate.of(2024, 1, 1), LocalTime.of(8, 0), LocalTime.of(17, 0));
        String expectedToString = "Attendance{id=1, employeeNumber='EMP001', lastName='Doe', firstName='John', date=2024-01-01, logIn=08:00, logOut=17:00}";
        assertEquals(expectedToString, attendance.toString());
    }

    @Test
    void testEqualsAndHashCode_EqualObjects() {
        Attendance attendance1 = new Attendance();
        attendance1.setEmployeeNumber("EMP001");
        attendance1.setDate(LocalDate.of(2024, 1, 1));

        Attendance attendance2 = new Attendance();
        attendance2.setEmployeeNumber("EMP001");
        attendance2.setDate(LocalDate.of(2024, 1, 1));

        assertEquals(attendance1, attendance2);
        assertEquals(attendance1.hashCode(), attendance2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_NotEqualObjects() {
        Attendance attendance1 = new Attendance();
        attendance1.setEmployeeNumber("EMP001");
        attendance1.setDate(LocalDate.of(2024, 1, 1));

        Attendance attendance2 = new Attendance();
        attendance2.setEmployeeNumber("EMP002");
        attendance2.setDate(LocalDate.of(2024, 1, 1));

        assertNotEquals(attendance1, attendance2);
        assertNotEquals(attendance1.hashCode(), attendance2.hashCode());
    }
}
