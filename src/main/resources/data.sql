-- Initialize admin user if not exists
INSERT IGNORE INTO users (name, email, password, role, active, first_login) 
VALUES ('System Administrator', 'admin@hospital.com', 'admin@123', 'ADMIN', 1, 0);
