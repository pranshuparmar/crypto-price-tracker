FROM amazoncorretto:17
MAINTAINER pranshu.parmar@gmail.com
ENTRYPOINT ["java","-jar","target/crypto-price-tracker-0.0.1.jar"]