CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   name TEXT not null,
   description TEXT not null,
   created TIMESTAMP not null,
   done BOOLEAN not null
);