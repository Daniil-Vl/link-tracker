--liquibase formatted sql
--changeset Daniil-Vl:3

CREATE TABLE chats_links
(
    chat_id BIGINT NOT NULL REFERENCES chat (chat_id) ON DELETE CASCADE,
    link_id BIGINT NOT NULL REFERENCES link (id) ON DELETE CASCADE,

    PRIMARY KEY (chat_id, link_id)
);
