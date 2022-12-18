CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   name TEXT not null,
   login TEXT unique not null,
   password TEXT not null
);

comment on table users is 'Таблица пользователей приложения';
comment on column users.id is 'Уникальный идентификатор пользователя';
comment on column users.name is 'Имя пользователя, которое видимо в приложении';
comment on column users.login is 'Логин, используемый для входа в систему';
comment on column users.password is 'Пароль, используемый для входа в систему';