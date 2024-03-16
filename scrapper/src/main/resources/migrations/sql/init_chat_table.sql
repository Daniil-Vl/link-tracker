--liquibase formatted sql
--changeset Daniil-Vl:1

CREATE TABLE chat
(
    chat_id BIGINT NOT NULL,

    PRIMARY KEY (chat_id),
    UNIQUE (chat_id)
);
