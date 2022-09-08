# REST-library
A simple REST API that represent Library.

There are 3 tables: `Author`, `Genre` and `Book`.

One Author has many Books (as Genres has many Books) and for Book it is mandatory to have one Author and one Genre in database.

## Getting started
1. Install latest Java version;
2. Install maven;
3. Clone project:
```
git clone https://github.com/RoshS/REST-library.git
```
4. Load root project folder in terminal
```
cd REST-library-API
```
and run command:
```
mvn spring-boot:run
```
or import project as Maven project and just run main method in `App` class from `src/main/java/ua/com/api/app` package;  
5. Swagger with documentation will be available on:
```
http://localhost:8080/swagger-ui.html
```

## Database configs
The API will up on built in database `H2`.  
H2 database files will be stored by `src/main/resources/db`, so if you restart API you don't lose your data.  
Also, you can change DB file location if you want:
```
spring.datasource.dbpath=./src/main/resources/db
```

### H2 workbench
If you use built in H2 database, you can reach something like SQL Workbench by link:
```
http://localhost:8080/h2-workbench
```
On opened web-page all inputs must be filled rightly. Click `Connect` button there. You are in!

### Using MySQL 
There is also opportunity to use MySQL DB. Follow next steps:
1. Open `src/main/resources/application.properties` file and enter credentials that will be suitable for your database:
```
spring.datasource.username=<db_user>
spring.datasource.password=<db_password>
```
2. Uncomment `MySQL` configs in `application.properties`:
```
spring.datasource.url=jdbc:mysql://localhost:3306/${spring.datasource.dbname}?serverTimezone=UTC&createDatabaseIfNotExists=true
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
```
and comment the same properties for `H2` database:
```
#spring.datasource.url=jdbc:h2:file:${spring.datasource.dbpath}/${spring.datasource.dbname};DB_CLOSE_ON_EXIT=FALSE
#spring.datasource.driverClassName=org.h2.Driver
#spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

## Authorization
In REST-library-API implemented simple basic authorization with next credentials: username:`admin` and pasword:`password`.

## Database filling
1. Go to `src/main/resources/scripts`;
2. Here you will find `fillDB.sql` with generated `INSERT` lines. Just execute this script from your database workbench.
3. It is also realized random data generation code in `DataGenrator` class by `src/main/java/ua/com/api` path. If you want to create other number of objects just change next values:
```
private final static int authorsCount = 150;
private final static int genresCount = 30;
private final static int booksCount = 1000;
```
Note the comments on these fields.  
The newly created lines will be rewritten in the same `fillDB.sql` file.
