create table tasks_categories (
id SERIAL PRIMARY KEY,
category_id int NOT NULL references categories(id),
task_id int NOT NULL references tasks(id)
);