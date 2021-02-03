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

### Running the frontend application

This one's easy. Just make sure you have node and npm.

```
npm install
npm start
```
