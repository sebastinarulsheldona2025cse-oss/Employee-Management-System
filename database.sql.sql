CREATE DATABASE IF NOT EXISTS employee_db;

USE employee_db;

 CREATE TABLE IF NOT EXISTS employees (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    salary DOUBLE
);