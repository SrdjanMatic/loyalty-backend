CREATE TABLE IF NOT EXISTS restaurant_config
(
    id                      SERIAL PRIMARY KEY,
    restaurant_id           BIGINT NOT NULL REFERENCES restaurant (id),
    logo                    TEXT,
    background_image        TEXT,
    background_color        VARCHAR(10),
    font_color              VARCHAR(10),
    header_and_button_color VARCHAR(10),
    restaurant_name         TEXT,
    description             TEXT
);
