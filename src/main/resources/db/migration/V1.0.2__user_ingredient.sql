CREATE TABLE measurement (
    id int primary key identity (1,1) not null,
    name varchar(255) not null,
    metric_quantity int not null,
    metric_unit varchar(15) not null
);

CREATE TABLE ingredient_category (
    id int primary key identity (1,1) not null,
    name varchar(255) not null
);

INSERT INTO ingredient_category (name) values ('Other');

CREATE TABLE user_ingredient (
    user_id int not null,
    ingredient_id int not null,
    metric_quantity int not null,
    custom_measurement_id int,
    date_added date not null default GETDATE(),
    date_last_used date,
    date_expiry date,
    constraint FK_user_ingredient_user foreign key (user_id) references [user](id),
    constraint FK_user_ingredient_custom_measurement foreign key (custom_measurement_id) references measurement(id),
    constraint FK_user_ingredient_ingredient foreign key (ingredient_id) references ingredient(id),
    constraint PK_user_ingredient primary key (user_id, ingredient_id)
);

ALTER TABLE ingredient ADD
    category_id int not null default 1,
    metric_unit varchar(15) not null default 'GRAMS',
    shelf_life_days int not null default 100;

ALTER TABLE ingredient ADD constraint FK_ingredient_category foreign key (category_id) references ingredient_category(id);

CREATE TABLE ingredient_measurement (
    ingredient_id int not null,
    measurement_id int not null,
    constraint FK_ingredient_measurement_ingredient foreign key (ingredient_id) references ingredient(id),
    constraint FK_ingredient_measurement_measurement foreign key (measurement_id) references measurement(id),
);