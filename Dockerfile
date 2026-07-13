FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN mkdir out \
 && javac -d out $(find . -name "*.java")

CMD ["java", "-cp", "out", "lb.LoadBalancer"]