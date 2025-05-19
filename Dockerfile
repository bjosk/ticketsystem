FROM eclipse-temurin:17-jdk

RUN apt-get update && apt-get install -y netcat-openbsd

WORKDIR /app

COPY target/*.jar app.jar
COPY wait-for-it.sh wait-for-it.sh

RUN chmod +x wait-for-it.sh

# Use wait-for-it with proper host and port
ENTRYPOINT ["./wait-for-it.sh", "db", "3306", "--", "java", "-jar", "app.jar"]
