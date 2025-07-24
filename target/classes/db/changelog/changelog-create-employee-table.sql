CREATE TABLE IF NOT EXISTS employee
(
    id         SERIAL PRIMARY KEY,
    company_id INTEGER     NOT NULL REFERENCES company (id),
    email      VARCHAR(50) NOT NULL UNIQUE ,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);