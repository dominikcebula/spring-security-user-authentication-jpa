create table users (
    user_id serial primary key,
	username varchar(64) not null,
	password varchar(64) not null,
	role varchar(50) not null,
	enabled boolean not null
);

create table persistent_logins (
    username varchar(64) not null,
    series varchar(64) primary key,
    token varchar(64) not null,
    last_used timestamp not null
);
