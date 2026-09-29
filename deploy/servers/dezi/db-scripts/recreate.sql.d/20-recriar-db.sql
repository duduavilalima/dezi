update pg_database set datallowconn = 'false' where datname = :'DB_NAME';
SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = :'DB_NAME' AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS :"DB_NAME";
CREATE DATABASE :"DB_NAME" WITH OWNER :'DB_USER' ENCODING 'UTF-8' TEMPLATE template0;
update pg_database set datallowconn = 'true' where datname = :'DB_NAME';
