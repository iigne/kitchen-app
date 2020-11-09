CREATE TABLE ingredient (
    id int not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

INSERT INTO ingredient (name) values ('Potato'), ('Carrot'), ('Apple'), ('Leek'), ('Lentils'), ('Spaghetti');