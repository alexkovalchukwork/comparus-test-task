DROP TABLE IF EXISTS user_table;
CREATE TABLE user_table (
                            ldap_login VARCHAR(50) PRIMARY KEY,
                            name VARCHAR(50),
                            surname VARCHAR(50)
);
INSERT INTO user_table (ldap_login, name, surname) VALUES ('ldap_login', 'jane_doe', 'Doe');
