CREATE TABLE cart (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
);