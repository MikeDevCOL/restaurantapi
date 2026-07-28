CREATE TABLE IF NOT EXISTS phone_number_prefixes (
  id BIGSERIAL PRIMARY KEY,
  prefix VARCHAR(10) NOT NULL,
  country VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS owners_contact_info (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(100) NOT NULL UNIQUE,
  phone_number VARCHAR(20) NOT NULL UNIQUE,
  phone_number_prefix_id BIGINT NOT NULL,
  CONSTRAINT fk_owners_contact_info_phone_prefix
    FOREIGN KEY (phone_number_prefix_id)
    REFERENCES phone_number_prefixes (id)
);

CREATE TABLE IF NOT EXISTS owners (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  contact_info_id BIGINT NOT NULL UNIQUE,
  CONSTRAINT fk_owners_contact_info
    FOREIGN KEY (contact_info_id)
    REFERENCES owners_contact_info (id)
);

CREATE TABLE IF NOT EXISTS restaurants (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(10) NOT NULL,
  owner_id BIGINT NOT NULL,
  CONSTRAINT uk_restaurants_code UNIQUE (code),
  CONSTRAINT fk_restaurants_owner
    FOREIGN KEY (owner_id)
    REFERENCES owners (id)
);
