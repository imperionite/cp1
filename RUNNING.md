## Commom CLI Commands Used

The MotorPH Payroll System - Phase 1

## **Project Description**  

The **MotorPH Payroll System - Phase 1** is a foundational implementation of an employee payroll management system built using **Spring Boot** and **MySQL**. The primary objective of this phase is to **automate the calculation of employee salaries** while ensuring an accurate presentation of employee details.  

```bash
# pull postgres image and run the container named mysql
docker run -d --name mysql -p 4306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=mydb -e MYSQL_USER=myuser -e MYSQL_PASSWORD=mypassword -v mysql-data:/var/lib/mysql mysql:8.0.40

# accessing the Running MySQL container
docker ps
docker exec -it mysql bash
mysql -h localhost -u myuser -p mydb
SELECT * FROM table_name; # basic select query
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
