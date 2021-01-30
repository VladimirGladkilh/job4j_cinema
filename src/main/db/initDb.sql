CREATE TABLE accounts (
   id SERIAL PRIMARY KEY,
   name TEXT,
   phone TEXT
);

CREATE TABLE halls (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE places (
   id SERIAL PRIMARY KEY,
   hallId INT,
   name TEXT,
   x INT,
   y INT,
   bussy BOOLEAN DEFAULT FALSE
);

CREATE TABLE tickets (
id SERIAL PRIMARY KEY,
userid int,
place int
);
CREATE UNIQUE INDEX uq_tickets
  ON tickets(userid, place);