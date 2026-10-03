DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id INT PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL
);

CREATE TABLE accounts (
    id INT PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customers(id),
    account_type VARCHAR(16) NOT NULL,
    balance DECIMAL(12, 2) NOT NULL
);

INSERT INTO customers (id, first_name, last_name) VALUES (1, 'Jordan', 'Reyes');
INSERT INTO customers (id, first_name, last_name) VALUES (2, 'Alex', 'Chen');

INSERT INTO accounts (id, customer_id, account_type, balance) VALUES (1001, 1, 'CHECKING', 500.00);
INSERT INTO accounts (id, customer_id, account_type, balance) VALUES (1002, 1, 'SAVINGS', 1500.00);
INSERT INTO accounts (id, customer_id, account_type, balance) VALUES (2001, 2, 'CHECKING', 750.00);
