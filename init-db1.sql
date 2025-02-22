CREATE TABLE IF NOT EXISTS users (
                                     user_id VARCHAR(255) PRIMARY KEY,
    login VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
    );

INSERT INTO users (user_id, login, first_name, last_name) VALUES
    ('example-user-id-1', 'user-1', 'User', 'Userenko');
