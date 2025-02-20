This section compiles the screenshots captured during the testing of REST API endpoints specifically related to `employee`. These endpoints are integral to fulfilling the requirements of MotorPH PHASE 1, which involves developing solutions using Java or Java-based technologies. In this instance, the implementation utilizes the Spring Boot framework to create the REST API. The tests were performed by executing actual HTTP requests through the [REST Client extension for Visual Studio Code (VS Code)](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).


## Create new employee - Admin only

POST /api/employees

![create employee](https://drive.google.com/uc?id=1f6_0-idIPrgjI1ak5RAigIa0ytp57vj2)


## Current user full employees details - Auth user

GET /api/employees/me

![me](https://drive.google.com/uc?id=1yGmblHWAA59NYvlz6Kxfk6u-phq88783)

## List all employee's basic info - Auth user

GET /api/employees/basic-info

![basic info](https://drive.google.com/uc?id=1B1cL1IfDZMNEhHIwc6Dh_ufOrT-ar_wQ)


## List all employee's basic info and user relationship - Admin only

GET /api/employees/admin

![employees with user obj](https://drive.google.com/uc?id=1mbkqOViqRaAv7yNAcPsz1dn8wrnB-HvD)


## Fetch employee by employee number - Admin only

GET /api/employees/employeeNumber/{employeeNumber}

![employees by employee number](https://drive.google.com/uc?id=1x4Ak3NCD9cN0BcgjwvD_Gvb4v0EFeNBb)


## Fetch employee by Id - Admin only

GET /api/employees/employeeNumber/{id}

![employees by id](https://drive.google.com/uc?id=19f0eiOB5deKNJMZYC2_DykeDDzWbZcWs)