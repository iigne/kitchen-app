create database kitchenapp;
use kitchenapp;
create login kitchenapp with password='VZa4u&VjFnRXn&!F62bQ';
create user kitchenapp for login kitchenapp;
exec sp_addrolemember 'db_owner', kitchenapp;