CREATE TABLE IF NOT EXISTS vip_restaurant
(
    id               SERIAL PRIMARY KEY,
    restaurant_id    BIGINT NOT NULL REFERENCES restaurant (id),
    created_at       TIMESTAMP      DEFAULT now(),
    discount         NUMERIC(15, 2) default 0,
    background_image TEXT
);