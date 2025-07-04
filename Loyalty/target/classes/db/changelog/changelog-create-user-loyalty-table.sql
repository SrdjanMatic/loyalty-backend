CREATE TABLE IF NOT EXISTS user_loyalty
(
    user_id       VARCHAR(50) NOT NULL,
    restaurant_id INTEGER     NOT NULL REFERENCES restaurant (id),
    points        INT       DEFAULT 0,
    joined_at     TIMESTAMP DEFAULT now(),
    active        boolean   default true,
    PRIMARY KEY (user_id, restaurant_id)
    );