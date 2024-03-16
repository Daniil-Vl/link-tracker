--liquibase formatted sql
--changeset Daniil-Vl:2

CREATE TABLE link
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    url             VARCHAR(300)                        NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE            NOT NULL,
    last_check_time TIMESTAMP WITH TIME ZONE            NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (id, url)
);
