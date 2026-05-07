CREATE SEQUENCE IF NOT EXISTS workers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE;

CREATE SEQUENCE IF NOT EXISTS organizations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE;

CREATE TABLE IF NOT EXISTS users (
    id       SERIAL PRIMARY KEY,
    login    VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL 
);

CREATE TABLE IF NOT EXISTS organizations (
    id              BIGINT PRIMARY KEY DEFAULT nextval('organizations_id_seq'),
    full_name       VARCHAR(255) UNIQUE,
    annual_turnover INTEGER CHECK (annual_turnover > 0),
    employees_count INTEGER NOT NULL DEFAULT 1,
    street          VARCHAR(255) NOT NULL,
    loc_x           BIGINT,
    loc_y           INTEGER,
    loc_z           INTEGER,
    CHECK (loc_x IS NULL OR (loc_y IS NOT NULL AND loc_z IS NOT NULL))
);

CREATE TABLE IF NOT EXISTS workers (
    id              BIGINT PRIMARY KEY DEFAULT nextval('workers_id_seq'),
    name            VARCHAR(255) NOT NULL,
    coord_x         INTEGER NOT NULL,
    coord_y         FLOAT NOT NULL,
    creation_date   TIMESTAMP NOT NULL,
    salary          INTEGER NOT NULL CHECK (salary > 0),
    start_date      TIMESTAMP NOT NULL,
    position        VARCHAR(50) NOT NULL,
    status          VARCHAR(50),           
    organization_id BIGINT REFERENCES organizations(id) ON DELETE SET NULL,
    owner_login     VARCHAR(100) NOT NULL REFERENCES users(login)
);