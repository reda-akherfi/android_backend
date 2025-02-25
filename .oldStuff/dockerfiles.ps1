# List of service names
$services = @(
    "document-service",
    "note-service",
    "video-service",
    "timer-service",
    "ai-chatbot-service",
    "dashboard-service",
    "auth-service",
    "notification-service",
    "configuration-service",
    "logging-service",
    "user-service",
    "module-service",
    "theme-service",
    "preference-service",
    "task-service",
    "statistics-service"
)

# Dockerfile content template
$dockerfileContent = @"
# Use an official OpenJDK runtime
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar into the container
ARG JAR_FILE=target/*.jar
COPY `${JAR_FILE}` app.jar

# Expose the application port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
"@

# Create Dockerfile for each service
foreach ($service in $services) {
    $path = "./$service/Dockerfile"

    # Check if the directory exists
    if (-Not (Test-Path "./$service")) {
        Write-Host "Directory '$service' not found. Skipping..." -ForegroundColor Yellow
        continue
    }

    # Write Dockerfile content
    $dockerfileContent | Out-File -FilePath $path -Encoding UTF8 -Force
    Write-Host "✅ Dockerfile created for $service" -ForegroundColor Green
}
