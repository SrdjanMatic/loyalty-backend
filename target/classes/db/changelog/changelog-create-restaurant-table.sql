CREATE TABLE IF NOT EXISTS restaurant
(
    id                   SERIAL PRIMARY KEY,
    name                 VARCHAR(255) NOT NULL,
    address              TEXT         NOT NULL,
    pib                  TEXT         NOT NULL UNIQUE,
    phone                VARCHAR(20),
    admin_keycloak_id    TEXT         NOT NULL,
    premium_coupon_limit NUMERIC      NOT NULL,
    vip_coupon_limit     NUMERIC      NOT NULL
);

