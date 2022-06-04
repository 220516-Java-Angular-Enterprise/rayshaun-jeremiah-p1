DROP SCHEMA IF EXISTS ers;
CREATE SCHEMA IF NOT EXISTS ers;
SET SCHEMA 'ers';

DROP TABLE IF EXISTS reimbursement_statuses;
CREATE TYPE REIMB_STATUS AS ENUM('Pending', 'Approved', 'Denied');
CREATE TABLE reimbursement_statuses (
	status_id INT PRIMARY KEY,
	status REIMB_STATUS UNIQUE
);

DROP TABLE IF EXISTS reimbursement_types;
CREATE TYPE REIMB_TYPE AS ENUM('lODGING', 'TRAVEL', 'FOOD', 'OTHER');
CREATE TABLE reimbursement_types (
	type_id INT PRIMARY KEY,
	type REIMB_TYPE UNIQUE
);

DROP TABLE IF EXISTS user_roles;
CREATE TYPE ROLES AS ENUM('ADMIN', 'FINANCE MANAGER', 'EMPLOYEE');
CREATE TABLE user_roles (
	role_id INT PRIMARY KEY,
	role ROLES UNIQUE
); 

DROP TABLE IF EXISTS users;
CREATE TABLE users (
	user_id VARCHAR PRIMARY KEY,
	username VARCHAR NOT NULL UNIQUE,
	email VARCHAR NOT NULL UNIQUE,
	password VARCHAR NOT NULL,
	given_name VARCHAR NOT NULL,
	surname VARCHAR NOT NULL,
	is_active BOOLEAN,
	role_id ROLES REFERENCES user_roles(role)
);

DROP TABLE IF EXISTS receipts;
CREATE TABLE receipts (
	num SERIAL PRIMARY KEY
);

DROP TABLE IF EXISTS reimbursements;
CREATE TABLE reimbursements (
	reimb_id VARCHAR PRIMARY KEY,
	amount DECIMAL(6,2) NOT NULL,
	submitted TIMESTAMP NOT NULL,
	resolved TIMESTAMP,
	description VARCHAR NOT NULL,
	receipt SERIAL REFERENCES receipts(num),
	payment_id VARCHAR,
	author_id VARCHAR NOT NULL REFERENCES users(user_id),
	resolver_id VARCHAR REFERENCEs users(user_id),
	status_id REIMB_STATUS NOT NULL REFERENCES reimbursement_statuses(status),
	type_id REIMB_TYPE NOT NULL REFERENCES reimbursement_types(type)
);