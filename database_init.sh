docker kill sql-server-db
docker rm sql-server-db
docker run --name sql-server-db -d -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=VZa4uVjFnRXnF62bQ' -p 1433:1433 mcr.microsoft.com/mssql/server:2019-latest

mssql -u sa -p VZa4uVjFnRXnF62bQ
