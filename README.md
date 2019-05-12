# REST-library
A simple REST API that represent Library. 

There are 3 tables: Author, Genre and Book. 

One Author has many Books (for Genre too) and one Book must has one Author and one Genre.

## Getting started
1. Clone project and import it like Maven project;
2. After that install mvn:
```
mvn clean install
```
3. Go to `application.properties` file and change values for this two rows that will be suitable for your MySQL Data Base:
```
spring.datasource.username=<username>
spring.datasource.password=<password>
```
4. Open MySQL Workbench and run the `create_DB.sql` script from `src/main/resources`;
5. Run main method in `App` class from `src/main/java/ua/com/epam/app` package;
6. Swagger with documentation will be available on:
```
http://localhost:8080/swagger-ui.html
```

## Data ingestion
It is realized random data ingestion in `DataIngestion` class by `src/main/java/ua/com/epam` path. To generate some random data run main method from this class;

It is possible to configure counts of every object:
```
private final static int authorsCount = 250;
private final static int genresCount = 30;
private final static int booksCount = 2000;
```

Max count of Genres is 30 (if you set more, generator works endlessly).

When generation will be finished, go to `resources`. Here will be generated `addData.sh` script file. Just run it and wait for DB filling.

After that you can fully feel all opportunities of REST Library API.
