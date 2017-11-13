CREATE TABLE IF NOT EXISTS user_profile (
  id        SERIAL PRIMARY KEY,
  user_id   INTEGER     NOT NULL REFERENCES users (id),
  level     INTEGER     NOT NULL,
  gameClass VARCHAR(64) NOT NULL
);