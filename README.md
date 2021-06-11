# P08_DAJAVA_SpringBoot_Docker

#Project Presentation

TourGuide is a travel and entertainment application.

#Quick Application Launch
* Build tourguide and gpsutil project with gradle.
* Install docker
* Open a console in tourguide project and type:

      docker compose up -d
    
That's all. A _docker-compose.yml_ file use the _dockerfile_ of each project to build docker image and container.

## You can get :
* All User's UUID and their location
  * http://localhost:8081/getAllCurrentLocations
* User's nearby attractions
  * http://localhost:8081/getNearbyAttractions?userName=internalUser0 
* User's trip deals
  * http://localhost:8081/getTripDeals?userName=internalUser0
* User's trip preferences
  * http://localhost:8081/getUserPreferences?userName=internalUser0
## You can set (POST):
* User's trip preferences
  * http://localhost:8081/setUserPreferences?userName=internalUser0
  * user's preferences JSON File (can be used with Postman Software)
   
        {
          "attractionProximity": 2147483647,
          "currency": "USD",
          "lowerPricePoint": 0,
          "highPricePoint": 400,
          "tripDuration": 1,
          "ticketQuantity": 1,
          "numberOfAdults": 1,
          "numberOfChildren": 0
        }  
        
## GPSUtil Microservice
 You can get:
 * User location by just giving his UUID:
   * http://localhost:8090/userLocation?userId=fded86b9-9c2d-43fd-ba02-65b7d7419645
 * All attractions:
   * http://localhost:8090/attractions
 
#Project Prerequisites and dependencies
The project need Java JDK 11 or newer. Open JDK is recommended: https://adoptopenjdk.net

The project use [Spring Boot 2.4.4](https://start.spring.io) with Gradle 5.6. 
 
    Docker 20.10.6+
    Java 11 JDK (or +)
    SpringBoot 2.4.4 
    Gradle 5.6 (or +)    
    junit-jupiter-api 5.7.1
    pojo-tester 0.7.6
    lombok 1.18.20
    
Intellij idea: don't forget to enable annotation processors for lombok. 
_Settings/Build-execution-deployment/compiler/annotationProcessors_ 
#Run app (on local port 8081)
## with Gradle
    gradle bootRun   

#Containerize main app (on local port 8081)
##Build the Jar of the project:
To get a _.jar_ file of the project in the _build/libs/_ folder:
###with console
    gradle clean
    gradle build
###with intellij
* Go to the gradle right vertical panel, expand it.
* Go to _Tasks/build_ 
     * double click on _clean_
     * double click on _build_

##Make a docker's image and run it
At project root:

    docker build -t tourguide .
    docker run -p 8081:8080 --name docker-tourguide tourguide
* _-d_ run the container in detached mode = in the background (not used here)
* _-p 8081:8080_ map port 8081 of the host to port 8080 in the container
* _--name docker-tourguide_ container name
* _tourguide_ the image to use

[source](https://docs.docker.com/get-started/)    
##Stop and delete docker's container and image
At project root:

    docker container ls -a (to retrieve container_id or name)
    docker container stop <container_id || container_name>
    docker container rm <container_id || container_name>
    docker rmi -f tourguide

#Containerize main app and all microservice 
We add a _docker-compose.yml_ in tourguide project to be able to use Docker Compose CLI. 

    docker-compose --version
    docker-compose config (for docker-compose file validation)
    docker compose up -d
    docker compose ps -a
    docker compose logs -f --tail 5  (show docker container logs)
    docker compose <stop || start>
    docker compose down  (resource destruction)
    docker rmi -f gpsutil
    docker rmi -f tourguide
* _-d_ run the container in detached mode = in the background

