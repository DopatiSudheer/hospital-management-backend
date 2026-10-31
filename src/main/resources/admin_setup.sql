-- Insert admin user directly into the database
-- Email: admin@hospital.com
-- Password: admin@123
-- This script should be executed once in your MySQL database

INSERT INTO users (name, email, password, role, active, first_login) 
VALUES ('System Administrator', 'admin@hospital.com', 'admin@123', 'ADMIN', true, false);
