# Smart kitchen assistant
A web app to help you remember about that cabbage at the back of the fridge.
[You can try it out here.](https://kitchenapp.azurewebsites.net/)

Using the deployed version is recommended rather than running it locally. 
However, if for some reason you wish to run it locally, here are the instructions:

## Prerequisites
In order to run this application, you will need these on your machine:
* Java 11
* Maven - [get it here](https://maven.apache.org/download.cgi)
* Docker - [get it here](https://docs.docker.com/get-docker/)
* Node.js and npm - [get it here](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm) 
* and obviously this repository, you will need to clone it
## Backend
#### Setting up database
We are using MS SQL Server for this. To avoid having to install that natively on our machines, we have it running from a Docker image:

`docker run --name sql-server-db -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=VZa4uVjFnRXnF62bQ' -p 1433:1433 mcr.microsoft.com/mssql/server:2019-latest`

#### Initialise the database
Once our database instance is running, we need to create the database, and the user that the application uses to connect to the database.

A CLI SQL tool is required for this. We can get it through `npm` by running:

`npm install -g sql-cli`

after that, run the script that spins up the docker container, creates the database and database user:

`sh database_init.sh`

#### Run the spring boot application

1. `mvn clean install`
2. Set active profile to local
3. launch the application.

Application will be running on port 8080. The React app interface will be available from there.

## Frontend

For development, it's better to run the frontend application separately, so that changes can be 
instantly applied without need for `mvn clean install`. 

We launch the React app with commands:

```
npm install
npm start
```

## Deployment

Deployment is done using GitHub actions now, but before it was implemented these were the instructions:

1. Ensure you’re on the master branch.
2. Replace `<REMOVED>` with credentials that have been removed from git in `application.yml`
3. Run `mvn clean install azure-webapp:deploy`. 

This will trigger the build, which will 
1. package up the React app to be served from the Spring Boot server
2. run the test suite
3. Push the generated application image to Azure App Service

### Infrastructure

The app is hosted on Azure App Service.

**To stop the app:**
1. Switch pricing plan to Free tier (this ensures you’re not charged)
2. Stop the app (from the dashboard)

**To check out the logs:**
 in the sidebar there is a `Log stream` option, this is where application logs reside