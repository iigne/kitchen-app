# A very pointless kitchen app 🥬
A web app that was supposed to make you remember about that cabbage at the back of the fridge.

This was a final year project for uni. This project has been mostly abandoned since then 😊 It's probably broken 🤷

## Prerequisites
In order to run this application, you will need these on your machine:
* Java 11
* Maven - [get it here](https://maven.apache.org/download.cgi)
* Docker - [get it here](https://docs.docker.com/get-docker/)
* Node.js and npm - [get it here](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)

## Backend

### Initialise the database
Once our database instance is running, we need to create the database, and the user the application uses to connect to the database.

A CLI SQL tool is required for this. We can get it through `npm` by running:

`npm install -g sql-cli`

after that, run the script that spins up the MS SQL docker container, creates the database and database user:

`sh database_init.sh`

### Run the spring boot application

1. `mvn clean install`
2. Set active profile to local
3. Launch the application.

## Frontend

For development, it's better to run the frontend application separately, so that changes can be 
instantly applied without need for `mvn clean install`. 

Launch the React app with commands:

```
npm install
npm start
```