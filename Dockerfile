FROM openjdk:22-jdk AS jre-build

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules jdk.unsupported,java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# Stage 2: Create the final image
FROM debian:bullseye-slim

# Install necessary libraries
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    libfontconfig1 \
    && rm -rf /var/lib/apt/lists/*

# Set environment variables
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Copy the custom Java runtime from the build st
COPY --from=jre-build /javaruntime $JAVA_HOME

COPY target/user-service-0.0.1.jar user-service.jar
ENTRYPOINT ["java","-jar","/user-service.jar"]
