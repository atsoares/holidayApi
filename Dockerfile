# base image
FROM openjdk:17-alpine
WORKDIR /opt/workdir/

# copy files over
COPY . .

# build the application
RUN ["./mvnw", "clean", "package", "-Dmaven.test.skip"]

# expose port 8080
EXPOSE 8080

# run the application
CMD ["java", "-jar", "target/holidayapi-0.0.1-SNAPSHOT.jar"]