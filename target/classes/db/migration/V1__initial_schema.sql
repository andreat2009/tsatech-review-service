CREATE TABLE review_product_review (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    author_name VARCHAR(128) NOT NULL,
    author_email VARCHAR(255),
    rating INT NOT NULL,
    title VARCHAR(255),
    text TEXT,
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT chk_review_product_rating CHECK (rating >= 1 AND rating <= 5)
);

CREATE INDEX idx_review_product_review_product ON review_product_review(product_id);
CREATE INDEX idx_review_product_review_approved ON review_product_review(approved);
