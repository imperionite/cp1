# **cp1**

A MotorPH Payroll System - Phase 1 coding project

This document serves as an extension of the primary README file, intended to explain, describe, and inform the reader about various aspects of the project in greater detail. It covers:

Implementation Rationale: A thorough explanation of the decisions made during the development process, including why certain technologies or methodologies were chosen over others.

Nuances: Detailed insights into specific features or functionalities of the project that may not be immediately apparent. This section aims to clarify complex components and their significance within the overall architecture.

Constraints: An overview of any limitations or constraints encountered during development. This includes technical limitations, design choices, and external factors that influenced project outcomes.

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

## Deductions

```markdown
## Weekly Deductions and Contributions Implementation

This document details the implementation of weekly deductions and contributions for SSS, Pag-Ibig, PhilHealth, and withholding tax within the system. It explains the calculation logic, data sources, and any specific constraints or nuances developers should be aware of.

### Core Calculation Logic

The system calculates weekly deductions by first determining the *monthly* contribution or premium amount and then dividing it by a factor to arrive at the weekly value.  The general formula used is:

```
Weekly Deduction = (Monthly Contribution or Premium) / (Weeks in a Month)
```

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
```

### PhilHealth Weekly Deduction

The `calculateWeeklyPhilHealthDeduction` method mirrors the SSS calculation.

1.  **Monthly PhilHealth Premium:** The `getMonthlyContribution` method is used to retrieve the monthly PhilHealth premium.

2.  **Weekly Calculation:** The monthly premium is divided by 4 to arrive at the weekly PhilHealth deduction.  The result is rounded to two decimal places using `RoundingMode.HALF_UP`.

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

The `calculateWeeklyWithholdingTax` method determines the weekly withholding tax.  This calculation is more complex as it involves tax brackets.

1.  **Weekly Taxable Income:** The employee's monthly salary is converted to a weekly salary by dividing it by 4.33.  The total weekly deductions (SSS, PhilHealth, Pag-Ibig) are then subtracted from this weekly salary to get the weekly taxable income.

2.  **Weekly Withholding Tax Calculation:** The weekly taxable income is then used to determine the applicable tax bracket from a weekly tax table. The tax is calculated based on the tax bracket and the excess amount over the lower limit of the bracket.  The weekly tax table is derived from the monthly tax table by dividing the bracket limits by 4.33.

```java
BigDecimal weeklySalary = monthlySalary.divide(BigDecimal.valueOf(4.33), RoundingMode.HALF_UP); // Divide by 4.33
// ... (calculate total weekly deductions)
BigDecimal taxableIncome = weeklySalary.subtract(totalDeductions);
return calculateWeeklyWithholdingTax(taxableIncome); // Use weekly tax table
```

### Key Considerations and Nuances

*   **Weeks in a Month:** The system uses 4 weeks for SSS, PhilHealth, and Pag-Ibig calculations.  The rationale for this is [ *Explain the rationale. Is it a fixed value used by the company, or is it based on a specific regulation?* ].
*   **Weekly Salary Calculation:**  The weekly salary for withholding tax calculation is derived by dividing the monthly salary by 4.33. The rationale for this is [ *Explain the rationale. Is it a fixed value used by the company, or is it based on a specific regulation?* ].  It's crucial to ensure this calculation aligns with company policy and relevant regulations.
*   **Rounding:** `RoundingMode.HALF_UP` is used for all deduction calculations to ensure proper financial rounding.
*   **Tax Table Accuracy:** The accuracy of the withholding tax calculation depends on the accuracy and up-to-dateness of the tax table.  It's essential to regularly review and update this table to comply with current tax regulations.
*   **Data Integrity:** Validating the data loaded from `contributions.json` is crucial.  Consider adding checks to ensure that the data is in the correct format and within reasonable bounds.
*   **Error Handling:** The system uses exceptions to handle cases where employee data is not found or when there are issues with the contribution data.  These exceptions are logged for debugging purposes.