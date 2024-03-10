--liquibase formatted sql
--changeset kosandron:1
create table Chats(
    id BIGINT PRIMARY KEY not null
);

create table Links(
    id BIGSERIAL PRIMARY KEY not null,
    url TEXT not null,
    last_check_time TIMESTAMP with time zone not null DEFAULT CURRENT_TIMESTAMP,
    created_by TEXT not null ,
    created_at TEXT not null,
    last_modified_time TIMESTAMP with time zone not null
);

create table chats_links (
    chat_id BIGINT not null,
    link_id BIGINT not null,
    PRIMARY KEY(chat_id, link_id)
);
