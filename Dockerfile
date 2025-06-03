# =========================
# STEP 1: Build avec Maven
# =========================
FROM maven:3.8.3-openjdk-17-slim AS builder

WORKDIR /app

# Copie uniquement le pom pour profiter du cache Docker
COPY pom.xml .

# Copie le code source
COPY src ./src

# Build le projet et générer le jar, sans tests pour gagner du temps
RUN mvn clean package -DskipTests

# =========================
# STEP 2: Runtime Distroless
# =========================
FROM gcr.io/distroless/java17-debian11

WORKDIR /app

# Copier uniquement le jar construit depuis l'étape builder
COPY --from=builder /app/target/migration-app.jar .

# Expose le port utilisé par l'application
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java","-jar","migration-app.jar"]
