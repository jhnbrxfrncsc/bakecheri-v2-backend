CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,

    cart_id BIGINT NOT NULL,

    product_id BIGINT NOT NULL,

    quantity INT NOT NULL,

    CONSTRAINT fk_cart
        FOREIGN KEY (cart_id)
        REFERENCES cart(id),

    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);