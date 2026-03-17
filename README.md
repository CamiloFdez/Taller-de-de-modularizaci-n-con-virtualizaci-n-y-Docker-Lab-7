# Taller de modularización con virtualización y Docker Lab 7

---

# Table of Contents
- [Project description](#project-description)
- [Technologies used](#technologies-used)
- [Project Structure](#project-structure)
- [Architecture Description](#architecture-description)
- [Installation and Usage](#installation-and-usage)
- [Docker Containerization](#docker-containerization)
- [Docker Hub](#docker-hub)
- [AWS EC2 Deployment](#aws-ec2-deployment)
- [Tests](#tests)
- [Video demonstration](#video-demonstration)
- [Conclusion](#conclusion)
- [Author](#author)

---

# Project description

This project demonstrates how to deploy a Java REST application using Docker containerization and cloud deployment on AWS EC2.

The application was developed using a lightweight Java microframework capable of scanning controllers dynamically and registering REST endpoints using annotations. The objective of this laboratory is to package the application inside a Docker container, publish it in Docker Hub, and deploy it on a cloud instance in AWS.

The system allows users to access REST services through a browser or HTTP client, demonstrating how containerization simplifies deployment and scalability.

---

# Technologies used
- Java
- Docker
- AWS EC2
- RESTful APIs
- Maven (for Java project management)
- Docker Hub (for container registry)
- HTTP

---

# Project Structure
The project follows a layered architecture separating controllers, framework logic, and configuration.

```text
src/main/java/arep/
├── controller
    ├── HelloController.java    // Example controller to demonstrate the framework
    ├── GreetingController.java // Another example controller for handling greeting requests
├── framework
    ├── HttpServer.java          // Core HTTP server implementation
    ├── HttpRequest.java         // Class representing an HTTP request
    ├── HttpResponse.java        // Class representing an HTTP response
    ├── WebFramework.java         // Main class for the web framework, responsible for scanning and registering controllers
    ├── WebMethod.java            // Class representing a web method, including HTTP method and path
    ├── MicroSpringBootApplication.java // Main application class to start the server
├── annotations
    ├── GetMapping.java            // Annotation for mapping GET requests
    ├── RequestParam.java             // Annotation for mapping query parameters
    ├── RestController.java             // Annotation for marking a class as a REST controller
├── Dockerfile                        // Dockerfile for building the application container
├── docker-compose.yml                   // Docker Compose file for orchestrating the application container
```

This modular structure allows the microframework to dynamically scan controller classes and register HTTP endpoints automatically.

---

# Architecture Description
The system architecture is composed of three main components:

## Java REST Application

The microframework processes HTTP requests using Java sockets and dynamically maps endpoints using annotations such as:

- @RestController
- @GetMapping
- @RequestParam

Example server log:

```bash
Escaneando controllers...
Registrando endpoint: /
Registrando endpoint: /hello
Registrando endpoint: /greeting
Registrando endpoint: /pi
Servidor iniciado en puerto 8080
Listo para recibir ...
```

Here is the image of the application running locally:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/runningProjectLocal.PNG)

## Docker Container

The application is packaged inside a Docker container which contains:

- Java runtime environment
- compiled application classes
- dependencies

This allows the application to run consistently across different environments.

## Cloud Deployment (AWS)

The container image is stored in Docker Hub and pulled from an AWS EC2 instance, where it runs inside a container and exposes the service to the internet.

Architecture flow:

```text
User Browser
     │
     ▼
AWS EC2 Instance
     │
     ▼
Docker Container
     │
     ▼
Java REST Microframework
```

---

# Installation and Usage

1. Clone the repository:

```bash
git clone https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7.git
```

2. Navigate to the project directory:

```bash
cd Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7
```

3. Build the project using Maven:

```bash
mvn clean package
```

4. Run locally:

```bash
java -cp target/classes:target/dependency/* edu.eci.dockerLab.framework.RestServiceApplication
```

Also we add to this lab threads and a shutdown hook to stop the server gracefully when the application is stopped. Here is the image of the application running locally:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/testingThreads.PNG)

5. Access the application in your browser:

```
http://localhost:8080/index.html
```

Here is the image of the application running locally:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/runningWebPageLocal.PNG)

## Docker Containerization
You can see Dockerfile and docker-compose.yml for building and running the application in a container.

- Build the Docker image:

```bash
docker build --tag dockerlab7 .
```

- Run the Docker container:

```bash
docker run -d -p 34000:8080 --name dockerlab7url1 dockerlab7
docker run -d -p 34001:8080 --name dockerlab7url2 dockerlab7
docker run -d -p 34002:8080 --name dockerlab7url3 dockerlab7
```

Here are the images of the Docker container running locally:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/docker34000.PNG)

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/docker34001.PNG)

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/docker34002.PNG)

## Docker Hub
The image was uploaded to Docker Hub to allow deployment from any environment.

- Docker Hub repository:

```
https://hub.docker.com/repository/docker/camilofdez/dockerlab7
```

Here is the cration of the Docker Hub repository:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/creatingDockerHub.PNG)

- Pull the image from Docker Hub:

```bash
docker pull camilofdez/dockerlab7
```

Here is the push of the image to Docker Hub:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/pushDockerHub.PNG)

- Run the container from Docker Hub:

```bash
docker run -p 8087:8080 camilofdez/dockerlab7
```

Here is the page web running from the Docker container:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/docker8087.PNG)

---

# AWS EC2 Deployment
To deploy the application in the cloud, an AWS EC2 instance was created.

1. Create an EC2 instance with a suitable configuration (e.g., t2.micro).
2. Connect to the instance using SSH.
3. Install Docker on the EC2 instance:

```bash
sudo yum update -y
sudo yum install docker -y
sudo service docker start
sudo usermod -a -G docker ec2-user
```

Here are the images of the Docker installation process on AWS EC2:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/sudoDockerUpdaye.PNG)  

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/serviceDocker.PNG)

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/dockerPsAws.PNG)

4. Pull the Docker image from Docker Hub:

```bash
docker pull camilofdez/dockerlab7
```

5. You can run the container on the EC2 instance, but normally you dont have to do it because the container is configured to run on startup. If you want to run it manually, use:

```bash
docker run -p 8080:8080 camilofdez/dockerlab7
```

6. Open the application in your browser using the public IP of the EC2 instance:

```
http://<EC2_PUBLIC_IP>:42000/index.html
```

Here is the web page running on AWS EC2:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/webPageAws.PNG)

Before all of this, make sure to configure the security group of the EC2 instance to allow inbound traffic on port 42000.

Here is the image of how it looks security group configuration:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/port42000.PNG)

---

# Tests
The application can be tested by sending HTTP requests to the defined endpoints. For example:

| Endpoint                   | Description                              |
| -------------------------- | ---------------------------------------- |
| `/hello`                   | Returns a simple greeting message         |
| `/ `                     | Returns a welcome message                 |
| `/pi`                      | Returns the value of Pi                   |
| `/greeting?name=YourName` | Returns a personalized greeting message   |
| `/greeting`                | Returns a default greeting message        |  

Also in this lab you can run tests that are in `src/test/java/arep/` to verify the functionality of the application. You can run the tests using Maven:

```bash
mvn test
```

Here is the image of the tests running:

![image](https://github.com/CamiloFdez/Taller-de-de-modularizaci-n-con-virtualizaci-n-y-Docker-Lab-7/blob/main/images/tests.PNG)

---

# Video demonstration

The following video demonstrates all the http endpoints working correctly, the Docker container running locally, and the application deployed on AWS EC2.

YouTube Video:

```
https://youtu.be/MExj_wE_iRs?si=4X0TR8yCm9jEKTrE
```

---

# Conclusion
This project demonstrates the use of containerization with Docker to package and deploy a Java REST application in a reproducible environment.

By integrating Docker Hub and AWS EC2, the application can be deployed and accessed from anywhere through the internet. This approach simplifies application deployment and ensures consistent execution across different environments.

---

# Author
- Camilo Fernández
- GitHub: [CamiloFdez](https://github.com/CamiloFdez)