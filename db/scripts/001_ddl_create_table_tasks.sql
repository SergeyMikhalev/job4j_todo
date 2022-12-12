CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   name TEXT not null,
   description TEXT not null,
   created TIMESTAMP not null,
   done BOOLEAN not null
);

comment on table tasks is 'Задачи, которые нужно выполнить';
comment on column tasks.id is 'Уникальный идентификатор задачи';
comment on column tasks.name is 'Название задачи';
comment on column tasks.description is 'Описание задачи';
comment on column tasks.created is 'Дата-время создания задачи';
comment on column tasks.done is 'Выполнена ли задача';