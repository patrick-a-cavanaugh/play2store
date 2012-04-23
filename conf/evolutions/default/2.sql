# --- !Ups

ALTER TABLE image ADD COLUMN width INT NOT NULL;
ALTER TABLE image ADD COLUMN height INT NOT NULL;

# --- !Downs

ALTER TABLE image DROP COLUMN width;
ALTER TABLE image DROP COLUMN height;