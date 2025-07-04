CREATE TABLE IF NOT EXISTS coupon
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    description   TEXT NOT NULL,
    restaurant_id INTEGER NOT NULL REFERENCES restaurant (id),
    created_at    TIMESTAMP      DEFAULT now(),
    points        INT
);