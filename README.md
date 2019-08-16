# REST-library
A simple REST API that represent Library.

There are 3 tables: `Author`, `Genre` and `Book`.

One Author has many Books (for Genre too) and one Book must has one Author and one Genre.

## Getting started
1. Clone project:
```
git clone https://github.com/RoshS/REST-library.git
```

2. Open `src/main/resources/application.properties` file and enter instead `<data_base_name>` name you want for your data base:
```
spring.datasource.dbname=<data_base_name>
```
The API will up on built in data base H2 and DB file will store here: `src/main/resources/db`, so if you restart API you don't lose your data.
Also you can change DB file location if you want:
```
spring.datasource.dbpath=./src/main/resources/db
```

3. Load root project folder and run:
```
mvn spring-boot:run
```
Or import project as Maven project and just run main method in `App` class from `src/main/java/ua/com/epam/app` package;

4. Swagger with documentation will be available on:
```
http://localhost:8080/swagger-ui.html
```
## Data ingestion
It is realized random data ingestion in `DataIngestion` class by `src/main/java/ua/com/epam` path. To generate some random data run main method from this class;

It is possible to configure counts of every object:
```
private final static int authorsCount = 150;
private final static int genresCount = 30;
private final static int booksCount = 1000;
```
Note the comments on these fields.

When generation will be finished, go to `resources`. Here will be generated `addData.sh` script file. Just run it and wait for DB filling.

After that you can fully feel all opportunities of REST Library API.
