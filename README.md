# holidayApi
TechInterview BluestonePIM - RestAPI consuming external API to retrieve holidays name happening in the same day in different countries

# How to test locally
- Java 17 / Maven / Your favourite IDE / Git
- Port 8080 must be available
- Clone the repository with __git clone__
- 'docker-compose up' in the root directory to start the application
- Access the API methods with either swagger or postman

- example call in postman: http://localhost:8080/api/holiday?date=2023-03-04&countryCode1=BR&countryCode2=PL

# Swagger
http://localhost:8080/swagger-ui.html

# Tests
- HolidayControllerTest.java
- HolidayClientTest.java
