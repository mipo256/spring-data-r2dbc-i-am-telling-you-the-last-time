CREATE TABLE IF NOT EXISTS my_entity ( ID BIGINT PRIMARY KEY );
CREATE TABLE IF NOT EXISTS my_streaming_entity(ID BIGSERIAL PRIMARY KEY, TYPE TEXT);

INSERT INTO my_entity(ID) VALUES (1);
INSERT INTO my_streaming_entity(TYPE) VALUES
  ('STANDARD'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('STANDARD'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED'),
  ('ENHANCED');
