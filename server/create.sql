CREATE TABLE sensors(
  ID SERIAL PRIMARY KEY,
  ITEM TEXT NOT NULL,
  STAMP BIGINT NOT NULL,
  JSONSTR TEXT NOT NULL
);