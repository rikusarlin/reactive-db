CREATE TABLE IF NOT EXISTS person(
  id uuid NOT NULL,
  firstName varchar(80),
  lastName varchar(80),
  personNumber varchar(11) NOT NULL,
  birthDate date,
  gender varchar(20),
  email varchar(80),
  PRIMARY KEY (id)
  created timestamp,
  creator varchar(20),
  modified timestamp,
  modifier varchar(20),  
);

INSERT INTO PERSON (created, creator, modified, modifier, id, personNumber, firstName, lastName, birthDate, gender, email) VALUES
  ('2020-10-23 17:45:55.220415', 'rikusarlin', '2020-10-23 17:45:55.220416', 'rikusarlin', random_uuid(), '010170-901K','Rauli', 'Wnape', '1970-01-01', 'MALE', 'rauliwnape2@hotmail.com'),
  ('2020-10-23 17:45:55.220415', 'rikusarlin', '2020-10-23 17:45:55.220416', 'rikusarlin', random_uuid(), '010170-902L','Marke', 'Peerakpe', '1970-05-13', 'FEMALE', 'marke.peerakpe@yahoo.com'),
  ('2020-10-23 17:45:55.220415', 'rikusarlin', '2020-10-23 17:45:55.220416', 'rikusarlin', random_uuid(), '010170-903M','Walter', 'Nutbekk', '2003-01-02', 'MALE', 'walter.nutbekk@welho.com'),
  ('2020-10-23 17:45:55.220415', 'rikusarlin', '2020-10-23 17:45:55.220416', 'rikusarlin', random_uuid(), '010170-904N','Suvi-Tuulia', 'Retsetenpe', '2005-02-17', 'FEMALE', 'suvi-tuuli.retsenape@gmail.com');

