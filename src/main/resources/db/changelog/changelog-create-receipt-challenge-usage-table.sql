CREATE TABLE IF NOT EXISTS receipt_challenge_usage
(
    receipt_key           VARCHAR(100) NOT NULL REFERENCES receipt (receipt_key),
    challenge_template_id BIGINT       NOT NULL REFERENCES challenge_template (id),
    used_at               TIMESTAMP DEFAULT now(),
    PRIMARY KEY (receipt_key, challenge_template_id)
);