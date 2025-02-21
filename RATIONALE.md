# **cp1**

A MotorPH Payroll System - Phase 1 coding project

This document serves as an extension of the primary README file, intended to explain, describe, and inform the reader about various aspects of the project in greater detail. It covers:

Implementation Rationale: A thorough explanation of the decisions made during the development process, including why certain technologies or methodologies were chosen over others.

Nuances: Detailed insights into specific features or functionalities of the project that may not be immediately apparent. This section aims to clarify complex components and their significance within the overall architecture.

Constraints: An overview of any limitations or constraints encountered during development. This includes technical limitations, design choices, and external factors that influenced project outcomes.

---

````markdown
## Entities

This section describes the core entities used in the system, including `User`, `Employee`, and `Attendance`. These entities are persisted in the database and represent the fundamental data structures of the application.

### User Entity

The `User` entity represents a user of the system, with different roles and permissions.

```java
@Entity
@Table(name = "users")
// ... (Lombok annotations)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    // ... (timestamps)
}
```
````

- **`id`:** The primary key, automatically generated.
- **`username`:** The unique username for login.
- **`password`:** The user's password (should be securely hashed in a real-world application).
- **`isActive`:** A boolean indicating whether the user account is active.
- **`isAdmin`:** A boolean indicating whether the user has administrator privileges.
- **`createdAt`, `updatedAt`:** Timestamps for creation and update.

**Constraints and Considerations:**

- The `username` must be unique.
- The `password` should be stored securely (hashed and salted). The current code stores the password in plain text, which is a **serious security vulnerability** and should be addressed immediately.
- The `isActive` flag allows for deactivating user accounts without deleting them.
- The `isAdmin` flag controls access to administrative functionalities.

### Employee Entity

The `Employee` entity stores information about each employee.

```java
@Entity
@Table(name = "employees")
// ... (Lombok annotations)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_number", unique = true, nullable = false)
    private String employeeNumber;

    // ... (other fields: lastName, firstName, birthday, address, phoneNumber, sss, philhealth, tin, pagibig, status, position, immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlyRate, hourlyRate, createdAt, updatedAt)

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // ... (toString, equals, hashCode overrides)
}
```

- **`id`:** The primary key, automatically generated.
- **`employeeNumber`:** A unique identifier for each employee.
- **`lastName`, `firstName`:** Employee's last and first names.
- **`birthday`:** Employee's birthday.
- **`address`, `phoneNumber`:** Employee's contact information.
- **`sss`, `philhealth`, `tin`, `pagibig`:** Employee's SSS, PhilHealth, TIN, and Pag-Ibig numbers. These should be unique.
- **`status`:** Employee's employment status (e.g., active, terminated).
- **`position`:** Employee's job position.
- **`immediateSupervisor`:** Name or ID of the employee's supervisor.
- **`basicSalary`:** Employee's basic salary.
- **`riceSubsidy`, `phoneAllowance`, `clothingAllowance`:** Additional allowances.
- **`grossSemiMonthlyRate`, `hourlyRate`:** Salary rates (semi-monthly and hourly).
- **`createdAt`, `updatedAt`:** Timestamps for creation and update.
- **`user`:** A one-to-one relationship with the `User` entity, allowing an employee to be associated with a user account.

**Constraints and Considerations:**

- Several fields (`employeeNumber`, `lastName`, `firstName`, `birthday`, `address`, `phoneNumber`, `sss`, `philhealth`, `tin`, `pagibig`, `position`, `immediateSupervisor`, `basicSalary`, `riceSubsidy`, `phoneAllowance`, `clothingAllowance`, `grossSemiMonthlyRate`, `hourlyRate`) are marked as `@NotNull`, indicating that they are required.
- `employeeNumber`, `phoneNumber`, `sss`, `philhealth`, `tin`, and `pagibig` are marked as `unique`.
- The `toString()`, `equals()`, and `hashCode()` methods are overridden for better object representation and comparison. The `equals()` and `hashCode()` methods are based on the `id` field.

### Attendance Entity

The `Attendance` entity records employee attendance data.

```java
@Entity
@Table(name = "attendance")
// ... (Lombok annotations)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_number", nullable = false)
    private String employeeNumber;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "log_in", nullable = false)
    private LocalTime logIn;

    @Column(name = "log_out", nullable = false)
    private LocalTime logOut;

    // ... (toString, equals, hashCode overrides)
}
```

- **`id`:** The primary key, automatically generated.
- **`employeeNumber`:** The employee number.
- **`lastName`, `firstName`:** Employee's last and first names.
- **`date`:** The date of the attendance record.
- **`logIn`:** The login time.
- **`logOut`:** The logout time.

**Constraints and Considerations:**

- All fields are marked as `@NotNull`.
- The `equals()` and `hashCode()` methods are overridden, and they are based on the `employeeNumber` and `date` fields, implying that an employee can have only one attendance record per day. _(Verify if this is the intended behavior.)_

## Date and Time Formats

The API uses the following date and time formats:

- **Date:** `YYYY-MM-DD` (ISO 8601 format). This format is used for all date-related fields in the API requests and responses. For example: `2024-07-10`.
- **Time:** `HH:mm` (24-hour format). This format is used for all time-related fields. For example: `14:30` (for 2:30 PM).

**Rationale:** The ISO 8601 date format (`YYYY-MM-DD`) is an international standard and is widely recognized. It avoids ambiguity and ensures consistency across different systems. The 24-hour time format is also a standard and avoids confusion between AM and PM. Using these standard formats makes the API easier to use and integrate with other systems.

---

## Weekly Cut-off Dates

This API provides a way to retrieve the available weekly cut-off periods for attendance tracking. The `/api/attendance/weekly-cutoffs` endpoint returns a list of weekly start and end dates. These dates are derived from the earliest and latest attendance records in the database. This allows users to easily select the relevant week for viewing or calculating their work hours.

**Rationale:** Providing these pre-calculated weekly cut-offs simplifies the user experience. Users don't need to manually determine the start and end dates for each week; the API provides them with the available options based on existing attendance data. This also ensures consistency and avoids potential errors from manually entered dates.

---

## Calculating Weekly Hours

The `calculateWeeklyHours()` method calculates the total work hours for an employee within a given week (Monday to Sunday). It includes a 10-minute grace period for late logins.

1.  **Week Validation:** The method first validates that the provided `startDate` is a Monday and the `endDate` is a Sunday. An `IllegalArgumentException` is thrown if the dates are invalid.

2.  **Attendance Retrieval:** It fetches all attendance records for the specified employee and week from the database.

3.  **Work Hour Calculation:** It iterates through each attendance record:

    - Calculates the raw worked hours by finding the difference between the logout time and the login time.
    - Applies the grace period: If the login time is after 8:10 AM, the difference between the login time and 8:10 AM is subtracted from the worked hours.
    - Accumulates the daily worked hours to get the total weekly worked hours.

4.  **Rounding:** The final total worked hours are rounded to two decimal places using `RoundingMode.HALF_UP`.

## Generating Weekly Cut-offs

The `getWeeklyCutoffs()` method retrieves the available weekly cut-off periods (start and end dates) based on the minimum and maximum attendance dates in the database.

1.  **Min/Max Dates:** It queries the database to find the earliest and latest attendance dates.

2.  **Weekly Periods:** It generates a list of `WeeklyCutoffDTO` objects, where each object represents a week (Monday to Sunday) between the minimum and maximum attendance dates. The end date of a week is adjusted if it exceeds the maximum attendance date.

### Data Structures

The `Attendance` entity stores the attendance information for each employee. The `WeeklyCutoffDTO` is used to represent a weekly cut-off period. The specific fields of these classes (e.g., `employeeNumber`, `logIn`, `logOut`, `date`) would be detailed in the Entities section of the documentation.

### Assumptions and Constraints

- The database schema includes an `Attendance` table with the necessary fields (employee number, login time, logout time, date).
- The `AttendanceRepository` provides methods for querying attendance records by employee and date range, as well as finding the minimum and maximum attendance dates.
- The grace period for late logins is 10 minutes (8:10 AM).
- The `calculateWeeklyHours` method assumes that the provided `startDate` and `endDate` represent a valid week (Monday to Sunday).
- The `getWeeklyCutoffs` method assumes that the attendance data is continuous, meaning there are no gaps in the date range. _(Verify this assumption)_

---

## Deductions

```markdown
## Weekly Deductions and Contributions Implementation

This document details the implementation of weekly deductions and contributions for SSS, Pag-Ibig, PhilHealth, and withholding tax within the system. It explains the calculation logic, data sources, and any specific constraints or nuances developers should be aware of.

### Core Calculation Logic

The system calculates weekly deductions by first determining the _monthly_ contribution or premium amount and then dividing it by a factor to arrive at the weekly value. The general formula used is:
```

Weekly Deduction = (Monthly Contribution or Premium) / (Weeks in a Month)

````

The system uses a value of 4 for the number of weeks in a month for SSS, PhilHealth, and Pag-Ibig.  For withholding tax, the calculation is a bit more involved, as described later.

### Data Sources

Contribution rates and brackets for SSS, PhilHealth, and Pag-Ibig are loaded from a JSON file (`contributions.json`) at application startup. This externalized configuration allows for easy updates to contribution rates without requiring code recompilation.  The `Contributions` class is used to store this data in memory.

### SSS Weekly Deduction

The `calculateWeeklySssDeduction` method handles the weekly SSS deduction.

1.  **Monthly SSS Contribution:** The `getMonthlyContribution` method is called to retrieve the correct monthly SSS contribution based on the employee's basic salary. This method looks up the contribution from the SSS contribution table loaded from the JSON file.

2.  **Weekly Calculation:** The monthly SSS contribution is then divided by 4 to get the weekly SSS deduction. The result is rounded to two decimal places using `RoundingMode.HALF_UP` to ensure proper financial calculations.

```java
BigDecimal monthlySssContribution = getMonthlyContribution(basicSalary, contributions.getSss(), "SSS");
return calculateWeeklyDeduction(monthlySssContribution); // Divide by 4 and round
````

### PhilHealth Weekly Deduction

The `calculateWeeklyPhilHealthDeduction` method mirrors the SSS calculation.

1.  **Monthly PhilHealth Premium:** The `getMonthlyContribution` method is used to retrieve the monthly PhilHealth premium.

2.  **Weekly Calculation:** The monthly premium is divided by 4 to arrive at the weekly PhilHealth deduction. The result is rounded to two decimal places using `RoundingMode.HALF_UP`.

```java
BigDecimal monthlyPhilHealthPremium = getMonthlyContribution(basicSalary, contributions.getPhilhealth(), "PhilHealth");
return calculateWeeklyDeduction(monthlyPhilHealthPremium); // Divide by 4 and round
```

### Pag-Ibig Weekly Deduction

The `calculateWeeklyPagIbigDeduction` method calculates the weekly Pag-Ibig deduction.

1.  **Monthly Pag-Ibig Contribution:** The `getMonthlyPagIbigContribution` calculates the monthly Pag-Ibig contribution based on the basic salary and the Pag-Ibig contribution table. This method also caps the maximum monthly contribution at 100.

2.  **Weekly Calculation:** The monthly Pag-Ibig contribution is divided by 4 to calculate the weekly deduction. The result is rounded using `RoundingMode.HALF_UP`.

```java
BigDecimal monthlyPagIbigContribution = getMonthlyPagIbigContribution(basicSalary); // Includes capping
return calculateWeeklyDeduction(monthlyPagIbigContribution); // Divide by 4 and round
```

### Withholding Tax Weekly Deduction

The `calculateWeeklyWithholdingTax` method determines the weekly withholding tax. This calculation is more complex as it involves tax brackets.

1.  **Weekly Taxable Income:** The employee's monthly salary is converted to a weekly salary by dividing it by 4.33. The total weekly deductions (SSS, PhilHealth, Pag-Ibig) are then subtracted from this weekly salary to get the weekly taxable income.

2.  **Weekly Withholding Tax Calculation:** The weekly taxable income is then used to determine the applicable tax bracket from a weekly tax table. The tax is calculated based on the tax bracket and the excess amount over the lower limit of the bracket. The weekly tax table is derived from the monthly tax table by dividing the bracket limits by 4.33.

```java
BigDecimal weeklySalary = monthlySalary.divide(BigDecimal.valueOf(4.33), RoundingMode.HALF_UP); // Divide by 4.33
// ... (calculate total weekly deductions)
BigDecimal taxableIncome = weeklySalary.subtract(totalDeductions);
return calculateWeeklyWithholdingTax(taxableIncome); // Use weekly tax table
```
