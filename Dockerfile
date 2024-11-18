# Sử dụng OpenJDK 17 với bản slim (có thể thay đổi theo yêu cầu)
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Copy toàn bộ mã nguồn vào thư mục làm việc
COPY . /app

RUN chmod +x mvnw

# Build ứng dụng với Maven Wrapper
RUN ./mvnw clean package

# Chạy ứng dụng với file JAR được tạo ra
CMD ["java", "-jar", "target/NewServer-0.0.1-SNAPSHOT.jar"]
