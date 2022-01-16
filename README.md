# Farm data project

## System requirements

* Java 11 or later
* Maven 3 or later
* Docker and Docker Compose
* (Optional) PostgreSQL

## Technologies

I chose to build this application on Spring Boot which is a well documented framework with lots of 
helpful features for web development. The application uses Opencsv for CSV parsing and validation. 

Data is stored in PostgreSQL which is a general purpose database and can easily handle the small datasets of this demo project.
I'm using Spring Data JPA for data access because it provides all basic CRUD methods and queries.

## Example data

CSV files with example data are located in src/main/resources/data directory.

## Features

* CSV parsing and validation for data rows:
  * Sensor type must be temperature, rainfall or pH.
  * Datetime must be ISO datetime format.
  * Value must be numeric. 
    * pH must be between 0 - 14 
    * temperature must be between -50 and 100
    * rainfall must be between 0 and 500
    
 * Endpoints to query farm information and metrics by farm and sensor type:
   
   ```/api/v1/farm``` lists all farms in the database. 
   
   ```/api/v1/farm/{id}``` shows farm id and name for given farm.
    
   ```/api/v1/farm/{id}/{metric}``` lists all metric data for given farm filtered by metric type. 
   With start and end query parameters results can be filtered to specific date range:
   ````/api/v1/farm/1/ph?start=2019-01-01&end=2019-04-01````
  
## Running the application

You can run the application locally from command line or using docker compose. Initial data loading takes some time. 
You should see 'All data saved for farm!' logged on screen when data loading from one CSV file is ready. 

### Running application in dev mode from command line:

1. Make sure you have PostgreSQL available in localhost port 5432. The easiest way to start the database is to run it in Docker:
`docker run -d --name farmdb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres postgres:13-alpine`
2. Start the application with dev profile: `mvn spring-boot:run -Dspring.profiles.active=dev`

You'll find the application running in `http://localhost:8081` (note the port)

### Running application with docker compose

1. Set environment variables. Docker-compose.yml reads PostgreSQL username, password and database name from .env file 
which should be in project root directory. Create .env file with the following variables:
```
DB_USERNAME=<your_user>
DB_PASSWORD=<your_password>
DB_NAME=<database_name>
```
2. Build the application: `mvn clean package`
3. Run `docker-compose up` to start the application and the database

You'll find the application running in `http://localhost:8080`

## Testing application

`mvn clean test` executes both unit tests and integration tests.

## Issues and TODOs

* CSV files for initial data load are hardcoded in FarmDataApplication class.
* The program assumes that one CSV file contains data of one farm only. The application extracts farm name from the first data row.
* Trying to load CSV data for a farm that already exists in the database causes an error.
* Todo: endpoint(s) to query statistics

