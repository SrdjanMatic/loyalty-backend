CREATE TABLE IF NOT EXISTS vip_restaurant_discount
(
    vip_restaurant_id BIGINT NOT NULL REFERENCES vip_restaurant (id),
    level             TEXT   NOT NULL CHECK (level IN ('STANDARD', 'PREMIUM', 'VIP')),
    discount          NUMERIC(15, 2) DEFAULT 0,
    PRIMARY KEY (vip_restaurant_id, level)
);