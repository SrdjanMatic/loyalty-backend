CREATE TABLE IF NOT EXISTS user_notification_status
(
    id              SERIAL PRIMARY KEY,
    notification_id INTEGER     NOT NULL REFERENCES notification(id),
    user_id         TEXT        NOT NULL,
    status          VARCHAR(10) NOT NULL,
    created_at      TIMESTAMP DEFAULT now(),
    seen_at         DATE        NOT NULL,

    CONSTRAINT unique_notification_user UNIQUE (notification_id, user_id)
);