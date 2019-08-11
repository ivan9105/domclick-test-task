залогинимся:

sudo su - postgres

добавим текущего пользователя как роль:
createuser --interactive;

добавим базу данных
createdb generate_db;

psql -h localhost -U root -d generate_db -a -f repeat_sql.sql