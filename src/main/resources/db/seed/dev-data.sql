INSERT INTO roles (name) VALUES ('ADMIN'), ('CUSTOMER');

INSERT INTO users (username, password, role_id, email) VALUES 
('admin', '$2a$10$7Q1Z5F1J8G9K1J8G9K1J8O1J8G9K1J8G9K1J8O1J8G9K1J8G9K1J8O', 1, 'admin@example.com'),
('customer', '$2a$10$7Q1Z5F1J8G9K1J8G9K1J8O1J8G9K1J8G9K1J8O1J8G9K1J8G9K1J8O', 2, 'customer@example.com'),
('owner', '$2a$10$7Q1Z5F1J8G9K1J8G9K1J8O1J8G9K1J8G9K1J8O1J8G9K1J8G9K1J8O', 2, 'owner@example.com');

INSERT INTO phone_number_prefixes (prefix, country) VALUES ('+1', 'USA'), ('+44', 'UK'), ('+91', 'India');

INSERT INTO owners_contact_info (phone_number, phone_number_prefix_id) VALUES 
('1234567890', 1),
('9876543210', 2),
('5555555555', 3);

INSERT INTO owners (first_name, last_name, contact_info_id, user_id) VALUES 
('John', 'Doe', 1, 1),
('Jane', 'Smith', 2, 2),
('Alice', 'Johnson', 3, 3);