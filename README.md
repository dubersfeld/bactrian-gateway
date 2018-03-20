# bactrian-gateway
I present here a microservice-oriented application that uses some basic Docker features including Docker Compose. It also uses Netflix Eureka, Netflix Zuul and Feign Client for service discovery and gateway implementation.

It is based on an initial project https://github.com/dubersfeld/bactrian 

Here are the prerequisites for running the complete application:

A recent Docker version installed (I used 17.12.0-ce)

A recent Apache Maven version installed (I used 3.3.9)

In addition I used Spring Tool Suite for developing this demo but it is not required for running the application.

The complete application is comprised of 8 containers, two persistence volumes and one bind. 

The frontend is only a web page server that uses Spring Security. It does not connect to any database, only send requests to the two REST servers customers-service and users-service. 

The REST server customers-service handles all CRUD requests to the customers database.
The REST server users-service handles the REST requests sent to the users database for authentication.

A separate Spring Cloud configuration server sets all deployment properties for the two REST servers and the frontend server that all have spring-cloud-config-client dependency. It fetches properties from the local file system. All configuration YAML files are stored in the subdirectory config-repo that is mounted as a bind. The configuration server URI and the profile are passed as environment variables from the docker-compose.yml file used to launch the application.

A separate Eureka server is used to register both services customers-service and users-services.

A separate Zuul server presents a unique port to the clients and route all REST requests to the REST servers.

Two Docker volumes are used for persisting the two databases when the application is stopped.

Here are the steps to run the application.

## 1. Database volumes creation

In subdirectory docker/customers run the commands:
```
customersBuild.sh
customersVolumes.sh
```

This creates a volume named bactrian\_customers\_db with a MySQL database named customers with a single prepopulated table customer.

You can check that the database creation was successful by connecting to the database container:

```
docker exec -it create_customers /bin/bash
```

Then connect to the database itself:

```
mysql -u dbuser -p
```

Then enter: `password1234`

You should be connected to the container MySQL server and the database should be present. 

Stop and remove the container with the command:

```
docker stop create_customers
```

**Stopping the create_customers container is required because it keeps the volume locked as long as it is running.**

In subdirectory docker/users run the commands:
```
usersBuild.sh
usersVolumes.sh
```

This creates a volume named bactrian\_users\_db with a MySQL database named users with two prepopulated tables user and user_Authority.

This database is populated with 5 users with different authority levels:

Username | Password  | Authorities
-------- | --------- | ----------------------------
Carol    | s1a2t3o4r | VIEW 
Albert   | a5r6e7p8o | VIEW, UPDATE
Werner   | t4e3n2e1t | VIEW, UPDATE
Alice    | o8p7e6r5a | VIEW, CREATE, UPDATE
Richard  | r1o2t3a4s | VIEW, CREATE, UPDATE, DELETE


Stop and remove the container with the command: `docker stop create_users`

## 2. Spring Boot images creation

In each of the 6 Maven project subdirectories:
confsvr
eureka-service
zuulsvr
users-service
customers-service
frontendsvr

run the command: `./mvnw clean package docker:build`

This will build the Spring Boot images:

bactrian/confsvr
bactrian/eureka-service
bactrian/zuulsvr
bactrian/users-servce
bactrian/customers-service
bactrian/frontendsvr

This command makes use of the docker-maven-plugin artifact that is included in all pom.xml files.


## 3. Launching the application
In subdirectory docker edit the file docker-compose.yml to match your own absolute path for config-repo bind mount. 
Then run the command in the same subdirectory: `docker-compose up`

This will start all services in the required sequence:
1. confsvr, eureka-service, zuul-service
1. users-service, customers-service
1. frontend

Now you can access each service from a web browser with the ports and paths listed below:

Server            | Port | Path
----------------- | ---- | ----------------------------------------
confsvr           | 8888 | customers-service/dev, users-service/dev
eureka-service    | 8761 | eureka/apps/customers-service, eureka/apps/users-service
zuul-service      | 5555 | routes, customers, users-service
customers-service | 8080 | customers-service/api/customerList
users-service     | 9090 | users-service/api/user/{username}
frontend          | 8090 | bactrian

Of course to execute a CRUD request you need to be granted the required authority. For example to be allowed to create a new customer you have to login as Alice or Richard etc.

You can dynamically add new instances of the customers-service image by running the shell script addCustomersService.sh and check that the round-robin load balancing works as expected by repeatedly sending the allCustomers request from a browser. 

You can also add a slow instance of the customers-service image by running the shell script addCustomersServiceSlow.sh and check that Zuul uses a fallback mechanism every time the response delay of the slow instance exceeds 1s.

An environment variable ENABLE_SLOW is used for demonstration purpose only to enable the random sleep delay.


To stop the application run the command in the docker subdirectory: `docker-compose down`

For a detailed tutorial on this project please follow this link:

www.dominique-ubersfeld.com/BACTRIAN/Bactrian5.html

