docker kill sql-server-db
docker rm sql-server-db
docker run --name sql-server-db -d -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=VZa4uVjFnRXnF62bQ' -p 1433:1433 mcr.microsoft.com/mssql/server:2019-latest

echo waiting for database to start up...
sleep 10

mssql -u sa -p VZa4uVjFnRXnF62bQ -q "create database kitchenapp;"
mssql -u sa -p VZa4uVjFnRXnF62bQ -d kitchenapp -q "create login kitchenapp with password='VZa4u&VjFnRXn&F62bQ';create user kitchenapp for login kitchenapp;exec sp_addrolemember 'db_owner', kitchenapp;"

echo SETUP COMPLETE!
