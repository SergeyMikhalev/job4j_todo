ALTER TABLE users ADD COLUMN time_zone text NOT NULL DEFAULT 'Europe/Moscow';

comment on column users.time_zone is 'Часовой поя пользователя';
