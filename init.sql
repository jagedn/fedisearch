CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS embeddings (
  id SERIAL PRIMARY KEY,
  username varchar(50),
  note text,
  acct varchar(100) unique,
  instance varchar(100),
  embedding vector,
  created_at timestamptz DEFAULT now()
);
