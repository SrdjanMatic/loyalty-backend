CREATE TABLE IF NOT EXISTS restaurant
(
    id                  SERIAL          PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    address             TEXT         NOT NULL,
    pib                 INT          NOT NULL,
    phone               VARCHAR(20),
    admin_keycloak_id   TEXT NOT NULL
    );