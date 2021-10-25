# maybank-currencies-service

Step to build & run
1. sync the code from https://github.com/kb-ong/maybank-currencies-service.git into directory "maybank-currencies-service"
2. go into directory "maybank-currencies-service" 
3. build ( mvn clean install )
4. run (mvn -f app\pom.xml spring-boot:run)

Step to test (using swagger)

http://localhost:8182/swagger

Step to test (using postman)

import maybank-currencies-service/maybank-currency-service-test.postman_collection.json into postman

Note :

DB : SQLITE ( maybank-currencies-service/app/maybank_db_test.db ) , so that no need to import into DB.

Java : make sure you are using version 11 and above.

Tomcat port: 8182



