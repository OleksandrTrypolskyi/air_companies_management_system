From openjdk:8
copy ./target/air_companies_management_system-0.0.1-SNAPSHOT.jar air_companies_management_system-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","employee-jdbc-0.0.1-SNAPSHOT.jar"]