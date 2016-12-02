CREATE DATABASE vi_recommender
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Slovak_Slovakia.1250'
       LC_CTYPE = 'Slovak_Slovakia.1250'
       CONNECTION LIMIT = -1;
	   
USE vi_recommender
CREATE EXTENSION postgis;
