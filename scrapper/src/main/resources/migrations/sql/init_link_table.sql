--liquibase formatted sql
--changeset Daniil-Vl:2

CREATE TABLE link
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE NOT NULL,
    url             VARCHAR(300) UNIQUE                        NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE                   NOT NULL,
    last_check_time TIMESTAMP WITH TIME ZONE                   NOT NULL,

    PRIMARY KEY (id)
--     UNIQUE (id, url)
);
