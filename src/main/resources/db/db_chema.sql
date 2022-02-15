-- DROP SCHEMA users_provider;

CREATE SCHEMA users_provider AUTHORIZATION app;

-- users_provider.users definition

-- Drop table

-- DROP TABLE users_provider.users;

CREATE TABLE users_provider.users (
	username varchar NULL,
	id int8 NOT NULL GENERATED ALWAYS AS IDENTITY
);