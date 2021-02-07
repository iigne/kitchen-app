# Smart kitchen assistant
A web app to help you remember about that cabbage at the back of the fridge.

## Running the application
## Backend
#### Setting up database
Annoyingly, we are using MS SQL Server for this... But thankfully, we have docker which makes our lives a little easier. 

`docker run --name sql-server-db -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=VZa4uVjFnRXnF62bQ' -p 1433:1433 mcr.microsoft.com/mssql/server:2019-latest`

#### Initialise the database
Once our database instance is running, we need to create the database, and the user that connects to the database.

We need some tools for this. Assuming we have `npm` we need to run:

`npm install -g sql-cli`

This allows us to have a simple cli interface with the database. When we have that we can do 

`mssql -u sa -p VZa4uVjFnRXnF62bQ`

and *we're in.* Now run some stuff to initialise the database itself

```
create database kitchenapp
use kitchenapp
create login kitchenapp with password='VZa4u&VjFnRXn&!F62bQ'
create user kitchenapp for login kitchenapp
exec sp_addrolemember 'db_owner', kitchenapp
```

Ideally, future me will put this all into a script that does everything for you.

#### Run the spring boot application

1. `mvn clean install`
2. Set active profile to local
3. launch the application.

Application will be running on port 8080. The React app interface will be available from there.

### Running the frontend application

For development, it's better to run the frontend application separately, so that changes can be 
instantly applied without need for `mvn clean install`. 

We launch the React app with commands:

```
npm install
npm start
```

## Deployment

1. Ensure you’re on the master branch.
2. Replace `<REMOVED>` with credentials that have been removed from git in `application.yml`
3. Run `mvn clean install azure-webapp:deploy`. 

This will trigger the build, which will 
1. package up the React app to be served from the Spring Boot server
2. run the test suite
3. Push the generated application image to Azure App Service

This ideally should be done through GitHub actions in the future!

### Infrastructure

The app is hosted on Azure App Service.

**To stop the app:**
1. Switch pricing plan to Free tier (this ensures you’re not charged)
2. Stop the app (from the dashboard)

**To check out the logs:**
 in the sidebar there is a `Log stream` option, this is where application logs reside