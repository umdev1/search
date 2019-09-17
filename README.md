# search
Search
This is a service provider project which accepts the text as input and provides the Books and albums with title and creator names. This also provides the result in sorted based on title.
MAx results from upstreams are by default 5 and configurable in property file for "max.result"

run the build using - mvn clean package
It will create search.jar in target folder 

go to target folder and run java -jar search.jar

It will start server and expose the urls 

swagger url - 
http://localhost:8000/swagger-ui.html

health check urls - 

http://localhost:8000/actuator

