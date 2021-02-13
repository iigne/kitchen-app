CREATE TABLE recipe (
    id int primary key identity (1,1) not null,
    title varchar(255) not null,
    image_link varchar(255),
    method text not null,
    author_user_id int,
    constraint FK_recipe_author_user foreign key (author_user_id) references [user] (id)
);

CREATE TABLE recipe_ingredient (
     recipe_id int not null,
     ingredient_id int not null,
     metric_quantity float not null,
     custom_measurement_id int,
     constraint FK_recipe_ingredient_recipe foreign key (recipe_id) references recipe(id),
     constraint FK_recipe_ingredient_custom_measurement foreign key (custom_measurement_id) references measurement(id),
     constraint FK_recipe_ingredient_ingredient foreign key (ingredient_id) references ingredient(id),
     constraint PK_recipe_ingredient primary key (recipe_id, ingredient_id)
);

CREATE TABLE user_recipe (
    user_id int not null,
    recipe_id int not null,
    constraint FK_user_recipe_user foreign key (user_id) references [user](id),
    constraint FK_user_recipe_recipe foreign key (recipe_id) references recipe(id)
)

