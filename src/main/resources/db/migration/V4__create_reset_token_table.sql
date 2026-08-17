CREATE TABLE reset_token (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    token_hash VARCHAR(250) NOT NULL,
    used BOOLEAN NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);