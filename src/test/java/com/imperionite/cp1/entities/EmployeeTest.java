package com.imperionite.cp1.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testEmployeeConstructorAndGetters() {
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        LocalDate now = LocalDate.now();

        Employee employee = new Employee(
                1L,
                "EMP001",
                "Doe",
                "John",
                birthday,
                "123 Main St",
                "123-456-7890",
                "SSS123",
                "Phil123",
                "TIN123",
                "Pag123",
                "Active",
                "Developer",
                "Supervisor",
                new BigDecimal("50000.00"),
                new BigDecimal("1000.00"),
                new BigDecimal("500.00"),
                new BigDecimal("200.00"),
                new BigDecimal("25000.00"),
                new BigDecimal("150.00"),
                now,
                now,
                new User()
        );

        assertEquals(1L, employee.getId());
        assertEquals("EMP001", employee.getEmployeeNumber());
        assertEquals("Doe", employee.getLastName());
        assertEquals("John", employee.getFirstName());
        assertEquals(birthday, employee.getBirthday());
        assertEquals("123 Main St", employee.getAddress());
        assertEquals("123-456-7890", employee.getPhoneNumber());
        assertEquals("SSS123", employee.getSss());
        assertEquals("Phil123", employee.getPhilhealth());
        assertEquals("TIN123", employee.getTin());
        assertEquals("Pag123", employee.getPagibig());
        assertEquals("Active", employee.getStatus());
        assertEquals("Developer", employee.getPosition());
        assertEquals("Supervisor", employee.getImmediateSupervisor());
        assertEquals(new BigDecimal("50000.00"), employee.getBasicSalary());
        assertEquals(new BigDecimal("1000.00"), employee.getRiceSubsidy());
        assertEquals(new BigDecimal("500.00"), employee.getPhoneAllowance());
        assertEquals(new BigDecimal("200.00"), employee.getClothingAllowance());
        assertEquals(new BigDecimal("25000.00"), employee.getGrossSemiMonthlyRate());
        assertEquals(new BigDecimal("150.00"), employee.getHourlyRate());
        assertEquals(now, employee.getCreatedAt());
        assertEquals(now, employee.getUpdatedAt());
        assertNotNull(employee.getUser());
    }

    @Test
    void testEmployeeSetters() {
        Employee employee = new Employee();
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        LocalDate now = LocalDate.now();

        employee.setId(1L);
        employee.setEmployeeNumber("EMP001");
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setBirthday(birthday);
        employee.setAddress("123 Main St");
        employee.setPhoneNumber("123-456-7890");
        employee.setSss("SSS123");
        employee.setPhilhealth("Phil123");
        employee.setTin("TIN123");
        employee.setPagibig("Pag123");
        employee.setStatus("Active");
        employee.setPosition("Developer");
        employee.setImmediateSupervisor("Supervisor");
        employee.setBasicSalary(new BigDecimal("50000.00"));
        employee.setRiceSubsidy(new BigDecimal("1000.00"));
        employee.setPhoneAllowance(new BigDecimal("500.00"));
        employee.setClothingAllowance(new BigDecimal("200.00"));
        employee.setGrossSemiMonthlyRate(new BigDecimal("25000.00"));
        employee.setHourlyRate(new BigDecimal("150.00"));
        employee.setCreatedAt(now);
        employee.setUpdatedAt(now);
        employee.setUser(new User());

        assertEquals(1L, employee.getId());
        assertEquals("EMP001", employee.getEmployeeNumber());
        assertEquals("Doe", employee.getLastName());
        assertEquals("John", employee.getFirstName());
        assertEquals(birthday, employee.getBirthday());
        assertEquals("123 Main St", employee.getAddress());
        assertEquals("123-456-7890", employee.getPhoneNumber());
        assertEquals("SSS123", employee.getSss());
        assertEquals("Phil123", employee.getPhilhealth());
        assertEquals("TIN123", employee.getTin());
        assertEquals("Pag123", employee.getPagibig());
        assertEquals("Active", employee.getStatus());
        assertEquals("Developer", employee.getPosition());
        assertEquals("Supervisor", employee.getImmediateSupervisor());
        assertEquals(new BigDecimal("50000.00"), employee.getBasicSalary());
        assertEquals(new BigDecimal("1000.00"), employee.getRiceSubsidy());
        assertEquals(new BigDecimal("500.00"), employee.getPhoneAllowance());
        assertEquals(new BigDecimal("200.00"), employee.getClothingAllowance());
        assertEquals(new BigDecimal("25000.00"), employee.getGrossSemiMonthlyRate());
        assertEquals(new BigDecimal("150.00"), employee.getHourlyRate());
        assertEquals(now, employee.getCreatedAt());
        assertEquals(now, employee.getUpdatedAt());
        assertNotNull(employee.getUser());
    }

    @Test
    void testToString() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeNumber("EMP001");
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setStatus("Active");

        String expectedToString = "Employee{id=1, employeeNumber='EMP001', lastName='Doe', firstName='John', status=Active}";
        assertEquals(expectedToString, employee.toString());
    }

    @Test
    void testEquals() {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        Employee employee2 = new Employee();
        employee2.setId(1L);
        Employee employee3 = new Employee();
        employee3.setId(2L);

        assertEquals(employee1, employee2);
        assertNotEquals(employee1, employee3);
    }
}