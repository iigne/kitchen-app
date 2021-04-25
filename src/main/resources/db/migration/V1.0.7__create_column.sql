ALTER TABLE user_ingredient ADD measurement_id int;
ALTER TABLE user_ingredient ADD constraint FK_user_ingredient_measurement foreign key (measurement_id) references measurement(id);