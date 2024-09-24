create table users (
    user_id serial primary key,
	username varchar(50) not null,
	password varchar(64) not null,
	role varchar(50) not null,
	enabled boolean not null
);
