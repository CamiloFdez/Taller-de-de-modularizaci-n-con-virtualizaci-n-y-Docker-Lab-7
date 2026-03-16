FROM openjdk:21-ea
 
WORKDIR /usrapp/bin
 
ENV PORT=8080
 
COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency

EXPOSE 8080
 
CMD ["java","-cp","./classes:./dependency/*","edu.eci.dockerLab.framework.RestServiceApplication"]