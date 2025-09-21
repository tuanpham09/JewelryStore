-- Create admin user and assign ADMIN role
USE jewelry;

-- Insert admin user
INSERT INTO users (email, password, full_name, phone_number, address, enabled, created_at, updated_at)
VALUES ('admin@jewelry.com', '$2a$10$cgziXouzLTHpKg2VcRYIkuwS/lePp/PxXYi6ecWKzTXHZi2eg86ve', 'Admin User', '0123456789', 'Admin Address', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    password = VALUES(password),
    full_name = VALUES(full_name),
    phone_number = VALUES(phone_number),
    address = VALUES(address),
    enabled = VALUES(enabled),
    updated_at = NOW();

-- Get user ID and role ID
SET @user_id = (SELECT id FROM users WHERE email = 'admin@jewelry.com');
SET @role_id = (SELECT id FROM roles WHERE name = 'ROLE_ADMIN');

-- Assign ADMIN role to user
INSERT INTO user_roles (user_id, role_id)
VALUES (@user_id, @role_id)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Show result
SELECT u.id, u.email, u.full_name, r.name as role_name
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
WHERE u.email = 'admin@jewelry.com';
