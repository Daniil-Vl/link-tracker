--liquibase formatted sql
--changeset Daniil-Vl:1

CREATE TABLE chat
(
    id      BIGINT NOT NULL,
    chat_id BIGINT NOT NULL,

    PRIMARY KEY (id)
);
