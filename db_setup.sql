CREATE DATABASE vi_recommender_train
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Slovak_Slovakia.1250'
       LC_CTYPE = 'Slovak_Slovakia.1250'
       CONNECTION LIMIT = -1;
	   
USE vi_recommender_train
CREATE EXTENSION postgis;


CREATE DATABASE vi_recommender_test
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Slovak_Slovakia.1250'
       LC_CTYPE = 'Slovak_Slovakia.1250'
       CONNECTION LIMIT = -1;
	   
USE vi_recommender_test
CREATE EXTENSION postgis;