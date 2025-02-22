DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       user_id VARCHAR(50) PRIMARY KEY,
                       login VARCHAR(50),
                       first_name VARCHAR(50),
                       last_name VARCHAR(50)
);
INSERT INTO users (user_id, login, first_name, last_name) VALUES ('1', 'john_doe', 'John', 'Doe');
