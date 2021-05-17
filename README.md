# P08_DAJAVA_SpringBoot_Docker

#Project Prerequisites and dependencies 
    Docker 20.10.6+
    Java 11 JDK (or +)
    SpringBoot 2.4.4 
    Gradle 5.6 (or +)    
    junit-jupiter-api 5.7.1
    pojo-tester 0.7.6
    lombok 1.18.20
Intellij idea: don't forget to enable annotation processors for lombok. 
_Settings/Build-execution-deployment/compiler/annotationProcessors_ 
#Run app (on local port 8080)
## with Gradle
    gradle bootRun    

#Containerize app (on local port 8081)
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

    docker container ls (to retrieve container_id or name)
    docker container stop <container_id || container_name>
    docker container rm <container_id || container_name>
    docker rmi -f tourguide




