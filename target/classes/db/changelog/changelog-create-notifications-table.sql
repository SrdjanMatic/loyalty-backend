CREATE TABLE IF NOT EXISTS notification
(
    id            SERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL REFERENCES restaurant (id),
    title         TEXT    NOT NULL,
    food_type    TEXT  NOT NULL,
    part_of_day  TEXT  NOT NULL,
    created_at    TIMESTAMP DEFAULT now(),
    valid_from    DATE    NOT NULL,
    valid_until   DATE
);

CREATE INDEX idx_notification_filtering
    ON notification (restaurant_id, food_type, valid_until);