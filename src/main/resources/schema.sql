DROP TABLE IF EXISTS measurement, farm, sensor;

CREATE TABLE IF NOT EXISTS farm (
	id SERIAL PRIMARY KEY,
	farm_name VARCHAR (255) UNIQUE
);

CREATE TABLE sensor (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE measurement (
  id SERIAL PRIMARY KEY,
  measurement_time TIMESTAMP WITH TIME ZONE,
  measurement_value DOUBLE PRECISION,
  sensor_type INTEGER REFERENCES sensor (id),
  farm_id INTEGER REFERENCES farm (id)
);


