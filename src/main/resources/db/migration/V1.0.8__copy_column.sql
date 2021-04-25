UPDATE user_ingredient SET measurement_id = custom_measurement_id;
ALTER TABLE user_ingredient ALTER COLUMN measurement_id int not null;