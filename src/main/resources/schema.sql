DROP TABLE IF EXISTS measurement, farm;
DROP CAST IF EXISTS (CHARACTER VARYING as sensor_type);
DROP TYPE IF EXISTS sensor_type;

CREATE TABLE IF NOT EXISTS farm (
	farm_id SERIAL PRIMARY KEY,
	farm_name VARCHAR (255) UNIQUE
);

CREATE TYPE sensor_type AS ENUM ('temperature', 'rainfall', 'pH');

CREATE TABLE measurement (
  measurement_id SERIAL PRIMARY KEY,
  measurement_time TIMESTAMP WITH TIME ZONE,
  measurement_value DOUBLE PRECISION,
  sensor_type sensor_type,
  farm_id INTEGER REFERENCES farm (farm_id)
);

CREATE CAST (CHARACTER VARYING as sensor_type) WITH INOUT AS IMPLICIT;

