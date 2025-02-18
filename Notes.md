**Employee Endpoints:**

1.  **`POST /api/employees` (Admin Only):** Creates a new employee. Requires an `Employee` object in the request body (including the `user` object with the user's ID) and authentication with the Admin role. Returns the created `Employee` object.

2.  **`GET /api/employees/admin` (Admin Only):** Retrieves all employees with full details. Requires authentication with the Admin role. Returns a list of `AdminEmployeeDTO` objects.

3.  **`GET /api/employees/basic-info` (Authenticated Users):** Retrieves basic information (employee number, name, birthday) for all employees. Requires authentication as any user. Returns a list of `EmployeeBasicInfoDTO` objects.

4.  **`GET /api/employees/me` (Authenticated Users):** Retrieves the full details of the currently logged-in employee. Requires authentication as any user. Returns a single `EmployeeFullDetailsDto` object.

5.  **`GET /api/employees/{id}` (Admin Only):** Retrieves an employee by their ID. Requires authentication with the Admin role. Returns a single `EmployeeFullDetailsDto` object.

6.  **`GET /api/employees/employeeNumber/{employeeNumber}` (All Users):** Retrieves an employee by their employee number. Accessible by all users (no authentication required). Returns a single `EmployeeFullDetailsDto` object.

**Key Considerations:**

*   **Security:**  Most of these endpoints are secured using Spring Security's `@PreAuthorize` annotation.  It's crucial to have Spring Security properly configured with roles (ADMIN, USER) and a mechanism to associate the employee number with the authenticated user (e.g., using custom claims or authorities in your JWT or authentication process).
*   **DTOs:**  The endpoints consistently use DTOs (`EmployeeFullDetailsDto`, `EmployeeBasicInfoDTO`, `AdminEmployeeDTO`) for responses.  This is a critical security best practice to avoid exposing sensitive or unnecessary data.
*   **Error Handling:** Basic error handling (e.g., returning 404 Not Found) is included in some endpoints.  More robust error handling can be added as needed.
