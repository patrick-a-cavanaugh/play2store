# --- !Ups

ALTER TABLE cart_line_item ADD COLUMN product_id BIGINT NOT NULL;

# --- !Downs

ALTER TABLE cart_line_item DROP COLUMN product_id;