CREATE TABLE ingredient (
    id int primary key identity (1,1),
    name varchar(255) not null,
);

INSERT INTO ingredient (name) values ('Potato'), ('Carrot'), ('Apple'), ('Leek'), ('Lentils'), ('Spaghetti');