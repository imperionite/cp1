This section compiles the screenshots captured during the testing of REST API endpoints specifically related to `attendance`. These endpoints are integral to fulfilling the requirements of MotorPH PHASE 1, which involves developing solutions using Java or Java-based technologies. In this instance, the implementation utilizes the Spring Boot framework to create the REST API. 

## Create new attendance - Admin only

POST /api/attendance

![create employee](https://drive.google.com/uc?id=1NwBgiemUU7p9izZS3QDf5tGCd5Kz9IuV)


## Attendance retrieval by Employee and Date Range - Auth user/employee only (as not all users are employees)

GET /api/attendance/employee/10001?startDate={startDate}&endDate={endDate}

![retrieve attendance](https://drive.google.com/uc?id=1aQQuCY5HyRZTVm8TpGOPxBepm1eWf3Hv)

## List of weekly cut-offs and available start datae and end date - Auth user

GET /api/attendance/weekly-cutoffs

![weekly cutoff](https://drive.google.com/uc?id=1ncKk2EFVDHjWEdAbJhQPjA_j8e5hrs0X)


## Calculate weekly work hours - Auth user

GET /api/attendance/employee/{employeeNumber}/weekly-hours?startDate={startDate}&endDate={endDate}

![worked hours](https://drive.google.com/uc?id=1JBipwkb8e4e_duNIMi2NTNbs8PaUX9O6)

## List all employees attendance records date range - Admin only

GET /api/attendance/admin?startDate={startDate}&endDate={endDate}

![work hours](https://drive.google.com/uc?id=1QjtVpyO2HgUJdAJhnQys7MOZC07UxOEQ)

## Calculate weekly hours worked (invalid dates) - Auth user

![incorrect date range](https://drive.google.com/uc?id=1Rij1Pq4msYtKogC82LfbS1HwsKorcWmt)

