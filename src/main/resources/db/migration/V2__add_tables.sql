CREATE TABLE IF NOT EXISTS users (
  id       SERIAL PRIMARY KEY,
  email    VARCHAR(64) UNIQUE            NOT NULL,
  nickname VARCHAR(25) UNIQUE            NOT NULL,
  password VARCHAR(60)                   NOT NULL,
  reg_date TIMESTAMP WITH TIME ZONE      NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS achievements (
  id          SERIAL PRIMARY KEY,
  title       VARCHAR(64)  NOT NULL,
  description TEXT         NOT NULL,
  image       VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS scores (
  id         SERIAL PRIMARY KEY,
  score_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  user_id    INTEGER REFERENCES users (id),
  score      INTEGER                  NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS progress (
  user_id        INTEGER REFERENCES users (id),
  achievement_id INTEGER REFERENCES achievements (id),
  completed      DECIMAL,
  CONSTRAINT unique_user_achievement_pair UNIQUE (user_id, achievement_id)
);