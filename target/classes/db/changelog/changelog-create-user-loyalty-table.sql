CREATE TABLE IF NOT EXISTS user_loyalty
(
    user_id          VARCHAR(50) NOT NULL,
    restaurant_id    BIGINT      NOT NULL REFERENCES restaurant (id),
    available_points BIGINT               DEFAULT 0,
    total_points     BIGINT               DEFAULT 0,
    level            TEXT        NOT NULL DEFAULT 'STANDARD' CHECK (level IN
                                                                    ('STANDARD', 'PROMOTED_TO_PREMIUM', 'PREMIUM',
                                                                     'PROMOTED_TO_VIP', 'VIP')),
    joined_at        TIMESTAMP            DEFAULT now(),
    active           boolean              default true,
    PRIMARY KEY (restaurant_id, user_id)
);

