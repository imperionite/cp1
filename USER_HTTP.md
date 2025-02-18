This section compiles the screenshots captured during the testing of REST API endpoints specifically related to `user`. These endpoints are integral to fulfilling the requirements of MotorPH PHASE 1, which involves developing solutions using Java or Java-based technologies. In this instance, the implementation utilizes the Spring Boot framework to create the REST API. The tests were performed by executing actual HTTP requests through the [REST Client extension for Visual Studio Code (VS Code)](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).

## Create new user - Admin only

POST /api/auth/register

![create user](https://drive.google.com/uc?id=1zBKv_EKZeoiN9kbiFEUXPBIL02uNxdeR)


## Fetch all users - Admin only

GET /api/users/

![all users](https://drive.google.com/uc?id=1X4jlDhpwjC7TYILm-K_8yZwPioKOk1Wd)

## User Auth Login - Public

POST /api/auth/login

![login](https://drive.google.com/uc?id=1yGmblHWAA59NYvlz6Kxfk6u-phq88783)