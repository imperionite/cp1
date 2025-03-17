# cp1

## Documentation for `AttendanceTest`


| Test Method                                      | Description                                                                                                                                     | Expected Behavior                                                                                                      |
|---------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| `testAttendanceConstructorAndGetters`             | Verifies that the `Attendance` class constructor and its getter methods work correctly.                                                           | The `Attendance` object is created using the constructor, and all getter methods return the correct values.              |
| `testAttendanceSetters`                           | Verifies that the setter methods in the `Attendance` class work as expected.                                                                      | The `Attendance` object is created using the default constructor, and setter methods are used to set values. The getter methods return the correct values. |
| `testToString`                                    | Verifies that the `toString` method in the `Attendance` class returns the expected string representation of an `Attendance` object.              | The `toString` method returns a string that matches the expected format, including all fields of the `Attendance` object. |
| `testEqualsAndHashCode_EqualObjects`              | Verifies that the `equals` and `hashCode` methods correctly compare two equal `Attendance` objects.                                               | Two `Attendance` objects with the same values are considered equal, and their hash codes are the same.                   |
| `testEqualsAndHashCode_NotEqualObjects`           | Verifies that the `equals` and `hashCode` methods correctly compare two different `Attendance` objects.                                           | Two `Attendance` objects with different values are considered not equal, and their hash codes are different.             |



![Attendance Test](https://drive.google.com/uc?id=12ObI-HlEkj1rYEhTQZKqdIwG7dt5uZya)

---

## Documentation for `AttendanceServiceTest`


| Test Method                                    | Description                                                                                                                                 | Expected Behavior                                                                                           |
|-------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| `testSaveAttendance`                            | Verifies that the `saveAttendance` method in `AttendanceService` calls the `save` method of the `AttendanceRepository`.                     | When the `saveAttendance` method is called with an `Attendance` object, the `save` method of `attendanceRepository` is called once. |
| `testGetAttendanceByEmployeeAndDateRange`       | Verifies that the `getAttendanceByEmployeeAndDateRange` method retrieves attendance records between the specified date range for an employee. | Given a date range and employee number, the method returns the expected list of attendance records.           |
| `testGetAttendanceByDateRange`                  | Verifies that the `getAttendanceByDateRange` method retrieves attendance records for the specified date range.                              | Given a date range, the method returns the expected list of attendance records.                             |
| `testCalculateWeeklyHours_ValidWeek_NoLateLogin`| Verifies that the `calculateWeeklyHours` method calculates the correct weekly hours when there is no late login.                            | The method calculates the weekly hours correctly (9.00 hours per day) without considering late logins.       |
| `testCalculateWeeklyHours_InvalidWeek_NotMondayToSunday`| Verifies that the `calculateWeeklyHours` method throws an `IllegalArgumentException` if the provided date range is not a valid week (Monday to Sunday). | The method throws an `IllegalArgumentException` when the start date is not a Monday or the end date is not a Sunday. |
| `testGetWeeklyCutoffs_NoAttendanceRecords`     | Verifies that the `getWeeklyCutoffs` method returns an empty list when no attendance records exist (both min and max dates are null).        | The method returns an empty list when there are no attendance records to calculate cutoffs.                 |


![Attendance Service Test](https://drive.google.com/uc?id=1OJiEDeup1_udQWwkJDO-kLF1FwG5jia-)

----

## Documentation for `AttendanceControllerTest`

| Test Method                                           | Description                                                                                                                                                      | Expected Behavior                                                                                                      |
|--------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| `testCreateAttendance_Success`                         | Tests the scenario where the attendance is created successfully. The employee is found, and the attendance is saved.                                            | The response status is `HttpStatus.CREATED`, indicating that the attendance was successfully created.                   |
| `testCreateAttendance_EmployeeNotFound`                | Tests the scenario where the employee is not found in the database.                                                                                             | The response status is `HttpStatus.BAD_REQUEST`, indicating the employee was not found.                                  |
| `testGetWeeklyCutoffs_Success`                         | Tests the scenario where weekly cutoffs are retrieved successfully.                                                                                             | The response status is `HttpStatus.OK`, and the response body contains a list of weekly cutoffs.                        |
| `testGetAttendanceByEmployeeAndDateRange_Success`      | Tests the scenario where attendance records for a specific employee and date range are retrieved successfully.                                                   | The response status is `HttpStatus.OK`, and the response body contains the list of attendance records.                  |
| `testGetAttendanceByDateRange_Success`                 | Tests the scenario where attendance records for a specific date range are retrieved successfully.                                                              | The response status is `HttpStatus.OK`, and the response body contains the list of attendance records.                  |
| `testCalculateWeeklyHours_Success`                      | Tests the scenario where weekly hours are successfully calculated.                                                                                              | The response status is `HttpStatus.OK`, and the response body contains the calculated total weekly hours.               |
| `testCalculateWeeklyHours_InvalidDateRange`            | Tests the scenario where an invalid date range is provided for calculating weekly hours.                                                                        | The response status is `HttpStatus.BAD_REQUEST`, indicating that the provided date range was invalid.                   |



![Attendance Controller Test](https://drive.google.com/uc?id=1QNwjNC65wg47XQF4v43hGCIaPza8BEio)