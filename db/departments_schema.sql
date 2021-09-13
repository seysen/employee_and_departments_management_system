DROP TABLE IF EXISTS DEPARTMENTS;

CREATE TABLE DEPARTMENTS (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
  name character varying(50) COLLATE pg_catalog."default",
  CONSTRAINT departments_pkey PRIMARY KEY (id)
);

