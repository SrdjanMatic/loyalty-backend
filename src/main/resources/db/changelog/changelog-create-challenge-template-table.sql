CREATE TABLE IF NOT EXISTS challenge_template
(
    id                   SERIAL PRIMARY KEY,
    restaurant_config_id BIGINT NOT NULL REFERENCES restaurant_config (id),
    period               INT    NOT NULL,
    visits_required      INT    NOT NULL
);
