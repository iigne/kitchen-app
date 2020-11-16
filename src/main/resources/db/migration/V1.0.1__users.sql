CREATE TABLE [user] (
    id int primary key identity (1,1) not null,
    username varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null
);

INSERT INTO [user] (username, email, password) VALUES ('admin', 'admin@admin.com', '$2b$10$5ARJkonpoxXNxibg5O1hZ.uoSclPGKdItM6v4QeKQ9oUKmTjHwn96')