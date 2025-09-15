-- Check if roles already exist before inserting
INSERT INTO roles (name) 
SELECT 'ROLE_USER' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name) 
SELECT 'ROLE_ADMIN' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name) 
SELECT 'ROLE_STAFF' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_STAFF');
