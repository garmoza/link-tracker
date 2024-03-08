--liquibase formatted sql

--changeset garmoza:1
CREATE TABLE IF NOT EXISTS tg_chat(
    id bigint,

    PRIMARY KEY (id)
);

--changeset garmoza:2
CREATE TABLE IF NOT EXISTS trackable_link(
    url text,
    last_change timestamp with time zone not null,

    primary key (url)
);

--changeset garmoza:3
CREATE TABLE IF NOT EXISTS subscribe(
    tg_chat_id bigint,
    trackable_link_url text,
    last_update timestamp with time zone not null,

    PRIMARY KEY (tg_chat_id, trackable_link_url),
    FOREIGN KEY (tg_chat_id) REFERENCES tg_chat(id),
    FOREIGN KEY (trackable_link_url) REFERENCES trackable_link(url)
);
