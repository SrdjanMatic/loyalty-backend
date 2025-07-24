CREATE TABLE IF NOT EXISTS coupon
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    description   TEXT        NOT NULL,
    level         TEXT        NOT NULL DEFAULT 'STANDARD' CHECK (level IN ('STANDARD', 'PREMIUM', 'VIP')),
    restaurant_id BIGINT     NOT NULL REFERENCES restaurant (id),
    created_at    TIMESTAMP            DEFAULT now(),
    points        INT
);

CREATE INDEX idx_coupon_restaurant_id ON coupon(restaurant_id);