
# Step 1: Builder le projet avec maven
FROM maven:3.8.3-openjdk-17-slim AS maven-builder
WORKDIR /app
COPY . /app
RUN mvn -f pom.xml clean package

# Step 2: Copier et lancer le .jar file
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=maven-builder ./app/target/migration-app.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "migration-app.jar"]
CMD ["tail", "-f", "/dev/null"]