CREATE TABLE shopping_user_ingredient
(
    user_id         int     not null,
    ingredient_id   int     not null,
    metric_quantity float   not null,
    measurement_id  int     not null,
    ticked          bit not null default 0,

    constraint FK_shopping_user foreign key (user_id) references [user](id),
    constraint FK_shopping_measurement foreign key (measurement_id) references measurement (id),
    constraint FK_shopping_ingredient foreign key (ingredient_id) references ingredient (id),
    constraint PK_shopping_user_ingredient primary key (user_id, ingredient_id)
);