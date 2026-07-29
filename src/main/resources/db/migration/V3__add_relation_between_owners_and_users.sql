ALTER TABLE owners
ADD COLUMN user_id BIGINT,
ADD CONSTRAINT fk_owners_user
  FOREIGN KEY (user_id)
  REFERENCES users (id);