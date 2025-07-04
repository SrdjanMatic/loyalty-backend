CREATE TABLE IF NOT EXISTS receipt
(
    receipt_key   VARCHAR(100) PRIMARY KEY,
    user_id       VARCHAR(50) NOT NULL,
    restaurant_id INTEGER     NOT NULL REFERENCES restaurant (id),
    amount        NUMERIC(15, 2) DEFAULT 0,
    created_at    TIMESTAMP      DEFAULT now(),
    points        INT
    );