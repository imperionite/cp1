# **cp1**  
The MotorPH Payroll System - Phase 1

## **Project Description**  

The **MotorPH Payroll System - Phase 1** is a foundational implementation of an employee payroll management system built using **Spring Boot** and **MySQL**. The primary objective of this phase is to **automate the calculation of employee salaries** while ensuring an accurate presentation of employee details.  

This system is designed to:  
✔️ **Display employee information** (employee number, name, and birthday).  
✔️ **Compute the number of hours worked per week** based on attendance records.  
✔️ **Calculate gross weekly salary** based on the total hours worked.  
✔️ **Apply basic deductions** to compute the net weekly salary.  

The **Phase 1 implementation** serves as the core structure of the payroll system, with future phases planned for expanding functionalities such as tax computation, benefits management, and employee records management.  

---

## **Requirements Scope**  

The **MotorPH Payroll System - Phase 1** is built upon the initial requirements identified in the **MotorPH System Milestone 1 Document** ([MS1.md](https://github.com/imperionite/cp1ms1/blob/main/MS1.md)). This document serves as the **foundation** for the system, explicitly defining the core functionalities required for Phase 1.  

However, the current repository ([cp1](https://github.com/imperionite/cp1)) also **incorporates additional considerations**, including implicit requirements and optional enhancements identified by the developer. These additional features are not part of the strict Phase 1 scope but have been **considered for future incremental improvements** as the project progresses. This respository served as the foundation of MS 2.

### **Key Distinctions:**  
✔️ **Explicit Requirements (MS1  repository)**: The non-negotiable functionalities required by MotorPH for Phase 1, such as employee detail presentation, work hours calculation, and payroll processing.  
✔️ **Implicit & Optional Considerations (cp1 repository)**: Additional refinements, optimizations, and potential enhancements that the developer has **factored into the system design** but will be implemented gradually based on feasibility and project scope.  

The primary focus remains on satisfying **Phase 1 requirements** first, while ensuring the system architecture allows for seamless expansion in future phases.  

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

### **API Endpoints Overview**  
For testing the available API functionalities, refer to the [rest.http](https://github.com/imperionite/cp1/blob/main/rest.http) file in the repository.

---

## Commom CLI Commands Used

```bash
# pull postgres image and run the container named mysql
docker run -d --name mysql -p 4306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=mydb -e MYSQL_USER=myuser -e MYSQL_PASSWORD=mypassword -v mysql-data:/var/lib/mysql mysql:8.0.40

# accessing the Running MySQL container
docker ps
docker exec -it mysql bash
mysql -h localhost -u myuser -p mydb
# quit mysql
quit
# quit interactive mode
ctrl+D

# clean slate
docker system prune -a && docker images prune -a && docker volume prune -a

# stop running conainer
docker stop mysql

# restart container
docker restart mysql 

# clean and build Spring Boot app
mvn clean && mvn install

# run Spring boot app
mvn spring-boot:run
```














