# Which "official Java image"/ Ke thua tu image nao
FROM openjdk:23-ea-16-oraclelinux8
# Working directory/ Thu muc lam viec
WORKDIR /app
# Copy from host(Laptop, PC dev) to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
# Run this inside image
RUN ./mvnw dependency:go-offline
CMD ["./mvnw", "spring-boot:run"]