# Backend Implementation

### Building

To Build the application execute the following. This will create a docker file in the local repo called curriculum/backend:latest 
```shell script
./gradlew build bootBuildImage --imageName=curriculum/backend
```

### Executing

Once you have the built the application, you can start it by executing
```shell script
docker run -p 8080:8080 -t curriculum/backend:latest
``` 

### Continuous Integration
The Git Hub project has enabled a continuous build upon creation of pull requests and merges to the Master branch.

### API View
Once you have started the docker image you can view the API documentation by visiting http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

### Notes:
Based upon the [Spring Doc Reference|https://github.com/springdoc/springdoc-openapi-demos/tree/master/springdoc-openapi-spring-boot-2-webmvc]
* Implements simple User
* Used a simple in memory HashMap Repository
* All other models from the demo API have been purged

