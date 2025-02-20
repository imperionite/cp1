# **cp1**  
The MotorPH Payroll System - Phase 1

## **Project Description**  

The **MotorPH Payroll System - Phase 1** is a foundational implementation of an employee payroll system built using **Spring Boot** and **MySQL**. The primary objective of this phase is to **automate the calculation of employee salaries** while ensuring an accurate presentation of employee details. This project derives from the requirements identification phase outlined in Phase 1, accessible via this [link](https://github.com/imperionite/cp1ms1/blob/main/MO-IT101%20Milestone%201_Imperial%20%2C%20Arnel_H1101_2nd%20Term_2024-2025.md).

This system is designed to:  
✔️ **Display employee information** (employee number, name, and birthday).  
✔️ **Compute the number of hours worked per week** based on attendance records.  
✔️ **Calculate gross weekly salary** based on the total hours worked.  
✔️ **Apply basic deductions** to compute the net weekly salary.  

### REST API Design

This project is designed as a RESTful API rather than a Java console application for several key reasons:

*   **Scalability:** A REST API can be easily scaled to handle a large number of requests.  It can be deployed on multiple servers and scaled horizontally as needed.
*   **Maintainability:**  A REST API architecture promotes modularity and separation of concerns, making the application easier to maintain and update.
*   **Integration:** A REST API can be easily integrated with other applications and front-end clients (web, mobile, etc.).  This allows for a more flexible and extensible system.
*   **Testability:** REST APIs are easier to test using tools like Postman or REST Client.  This simplifies the development and testing process.

**Rationale:** The REST API design was chosen to create a robust, scalable, and maintainable application that can be easily integrated with other systems and accessed by various clients.  This approach is more suitable for a production environment compared to a simple console application.

---

## **REST API Endpoints & Manual API Tests**

Refer to the following list of links to get an idea what are the REST API endpoints that are developed in the prohject and how HTTP requests are executed in [REST Client extension for Visual Studio Code (VS Code)](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).

1. [User: User registration, authentication and fetching users](https://github.com/imperionite/cp1/blob/main/USER_HTTP.md)
    - [Sample Users/Auth Requests](https://github.com/imperionite/cp1/blob/main/users.http)

2. [Employee: Displaying employee information, creating new employee, etc.](https://github.com/imperionite/cp1/blob/main/EMPLOYEE_HTTP.md)
    -  [Sample Employees Requests](https://github.com/imperionite/cp1/blob/main/employees.http)

3. [Attendance: Calculating employee's work hours, retrieving available weekly weekly cut-offs, displaying attendance records, creating new attendance](https://github.com/imperionite/cp1/blob/main/ATTENDANCE_HTTP.md)
    -  [Sample Attendance Requests](https://github.com/imperionite/cp1/blob/main/attendance.http)

---

## Initial Data Source and Application Setup

The initial data utilized by this application is sourced from the Google Sheets provided in the Phase 1 requirements instruction, accessible via this [link](https://sites.google.com/mmdc.mcl.edu.ph/motorph/home). Specifically, the [Employee](https://docs.google.com/spreadsheets/d/1168Un_0b5CPDwDSOH4CWI1m8_-2LpLadX3wAdUNNFOo/edit?usp=sharing) database serves as the foundational dataset. The process of seeding data is accomplished through the use of the `ApplicationRunner`.

### Data Seeding and Initial Setup

The application uses an `ApplicationRunner` to seed the database with initial data.  This ensures that the application has a basic set of employees and users to start with.  The data is prepared in a multi-step process:

1.  **Google Sheets:** Employee and user data is initially sourced in Google Sheets for ease of collaboration and management.
2.  **CSV Export:** This data is then exported from Google Sheets in CSV (Comma Separated Value) format.
3.  **MySQL Import:**  The CSV files are then used to populate the MySQL database during application startup using the `ApplicationRunner`.  This automated process ensures that the database is consistently populated with the necessary data.

**Rationale:** This approach allows for a flexible and manageable way to maintain and update the initial data.  Google Sheets provides a user-friendly interface for data entry and collaboration, while the CSV format serves as a reliable intermediate format for importing into the database.  The `ApplicationRunner` automates the import process, ensuring data consistency and reducing manual effort.

### Initial Tables and Data Overview

During the initial execution of the application, several tables and corresponding datasets are established. These tables form the core structure upon which further functionalities will be built.

[![DB Table](https://drive.google.com/uc?export=view&id=1eWGZmh_5VubQ87pKTJM5d66fBTGCnxR3)](https://drive.google.com/file/d/1eWGZmh_5VubQ87pKTJM5d66fBTGCnxR3/view?usp=sharing)

---

#### Users Table

[![Users Table](https://drive.google.com/uc?export=view&id=1eWGZmh_5VubQ87pKTJM5d66fBTGCnxR3)](https://drive.google.com/file/d/1eWGZmh_5VubQ87pKTJM5d66fBTGCnxR3/view?usp=sharing)

---

#### Employees Table

[![Employees Table](https://drive.google.com/uc?export=view&id=1DXsEzSAfoVljpL56ltlXn_PR0IdVsWi8)](https://drive.google.com/file/d/1DXsEzSAfoVljpL56ltlXn_PR0IdVsWi8/view?usp=sharing)

**prescribed**

[![Employees Table - prescribed](https://drive.google.com/uc?export=view&id=10MqVlvdk2bpgXQQ98vKMz82c36bMePEV)](https://drive.google.com/file/d/10MqVlvdk2bpgXQQ98vKMz82c36bMePEV/view?usp=sharing)

---

#### Attendance Table

[![Attendance Table](https://drive.google.com/uc?export=view&id=1au5gUECk_yItZT8FVTSU1sj972DgbkB4)](https://drive.google.com/file/d/10MqVlvdk2bpgXQQ98vKMz82c36bMePEV/view?usp=sharing)

## **Requirements Scope**  

The **MotorPH Payroll System - Phase 1** is built upon the initial requirements identified in the **MotorPH System Milestone 1 Document** ([MS1.md](https://github.com/imperionite/cp1ms1/blob/main/MS1.md)). This document serves as the **foundation** for the system, explicitly defining the core functionalities required for Phase 1.  

However, the current repository ([cp1](https://github.com/imperionite/cp1)) also **incorporates additional considerations**, including implicit requirements and optional enhancements identified by the developer. These additional features are not part of the strict Phase 1 scope but have been **considered for future incremental improvements** as the project progresses. This respository served as the foundation of MS 2.

### **Key Distinctions:**  
✔️ **Explicit Requirements (MS1  repository)**: The non-negotiable functionalities required by MotorPH for Phase 1, such as employee detail presentation, work hours calculation, and payroll processing.  
✔️ **Implicit & Optional Considerations (cp1 repository)**: Additional refinements, optimizations, and potential enhancements that the developer has **factored into the system design** but will be implemented gradually based on feasibility and project scope.  

The primary focus remains on satisfying **Phase 1 requirements** first, while ensuring the system architecture allows for seamless expansion in future phases.  

---

## Date and Time Formats

The API uses the following date and time formats:

*   **Date:** `YYYY-MM-DD` (ISO 8601 format).  This format is used for all date-related fields in the API requests and responses.  For example: `2024-07-10`.
*   **Time:** `HH:mm` (24-hour format).  This format is used for all time-related fields. For example: `14:30` (for 2:30 PM).

**Rationale:** The ISO 8601 date format (`YYYY-MM-DD`) is an international standard and is widely recognized.  It avoids ambiguity and ensures consistency across different systems.  The 24-hour time format is also a standard and avoids confusion between AM and PM.  Using these standard formats makes the API easier to use and integrate with other systems.

---

## Weekly Cut-off Dates

This API provides a way to retrieve the available weekly cut-off periods for attendance tracking.  The `/api/attendance/weekly-cutoffs` endpoint returns a list of weekly start and end dates.  These dates are derived from the earliest and latest attendance records in the database.  This allows users to easily select the relevant week for viewing or calculating their work hours.

**Rationale:** Providing these pre-calculated weekly cut-offs simplifies the user experience.  Users don't need to manually determine the start and end dates for each week; the API provides them with the available options based on existing attendance data.  This also ensures consistency and avoids potential errors from manually entered dates.

---

## **Getting Started**  

### **Prerequisites**  
Ensure that the following tools are installed on your system before setting up the project:  

- **Java 17**  
- **Maven 3.4.2**  
- **Docker & Docker Compose** (for database and frontend setup)  
- **MySQL 8** 

---

### **Project Setup**

Refer also to this [link](https://github.com/imperionite/cp1/blob/main/RUNNING.md) to the common CLI command used.

#### **1. Clone the Repository**  
```sh
git clone https://github.com/imperionite/cp1.git
cd cp1
```

#### **2. Set Up the Database**  
Run the following command to start the **MySQL database** using Docker:  
```sh
docker run -d --name mysql -p 4306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=mydb -e MYSQL_USER=myuser -e MYSQL_PASSWORD=mypassword -v mysql-data:/var/lib/mysql mysql:8.0.40
```
This will initialize a MySQL database instance configured for the application.

---

#### **4. Build and Run the Application**  
Compile and run the **Spring Boot** application:  
```sh
mvn spring-boot:run
```

The application should now be running on `http://localhost:8080`.

---















