create table users (
    user_id serial primary key,
	username varchar(64) not null,
	password varchar(64) not null,
	role varchar(50) not null,
	enabled boolean not null,
	UNIQUE (username)
);

create table persistent_logins (
    username varchar(64) not null,
    series varchar(64) primary key,
    token varchar(64) not null,
    last_used timestamp not null
);

create table activation_links (
    link_id serial primary key,
    user_id serial references users(user_id),
    token char(36) not null,
    expiry_date timestamp with time zone not null,
    UNIQUE (link_id, user_id)
);

create table password_reset_links (
    link_id serial primary key,
    user_id serial references users(user_id),
    token char(36) not null,
    expiry_date timestamp with time zone not null,
    UNIQUE (link_id, user_id)
);
