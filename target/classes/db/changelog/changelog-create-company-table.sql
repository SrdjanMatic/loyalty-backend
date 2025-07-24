CREATE TABLE IF NOT EXISTS company
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT now()
);