Air companies management system: Java, Spring Boot, Spring Web, Spring Data, MySQL, Docker.

In order to test this application please follow next steps:
- clone repository
``git clone git@github.com:OleksandrTrypolskyi/air_companies_management_system.git``;
- run ``maven clean install``
- you need to have docker installed and running;
- create docker network ``docker network create air_company-mysql``;
- confirm creation of network ``docker netowrk ls``;
- you need to have MySQL image downloaded ``docker pull mysql``;
- run MySQL container ``docker container run --name mysqldb --network air_company-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=air_company -d mysql:latest
  ``;
- confirm MySQL container is running ``docker ps``;
- 