CREATE TABLE user_food_preferences
(
    id         SERIAL PRIMARY KEY,
    user_id    TEXT         NOT NULL,
    preference VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE user_visit_preferences
(
    id          SERIAL PRIMARY KEY,
    user_id     TEXT        NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,
    part_of_day VARCHAR(20) NOT NULL,
    created_at  TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_user_food_preferences_user_id ON user_food_preferences (user_id);

CREATE INDEX idx_user_visit_preferences_user_id ON user_visit_preferences (user_id);
