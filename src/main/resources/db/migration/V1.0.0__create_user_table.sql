CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    fullname varchar(255) not null,
    email varchar(255) not null unique,
    username varchar(255) not null unique,
    password varchar(255) not null,
    created_at timestamptz default current_timestamp,
    modified_at timestamptz default current_timestamp
);