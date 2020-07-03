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

You can use docker-compose to launch the application as well as the ES instances by executing
```shell script
docker-compose up
``` 

### Continuous Integration
The Git Hub project has enabled a continuous build upon creation of pull requests and merges to the Master branch.

### API View
Once you have started the docker image you can view the API documentation by visiting http://localhost:8080



* Implements simple User
* Used a simple in memory HashMap Repository
* All other models from the demo API have been purged

