--liquibase formatted sql

--changeset garmoza:1
create table tg_chat(
    id bigint,

    primary key (id)
);

--changeset garmoza:2
create table trackable_link(
    url text,
    last_change timestamp with time zone not null,

    primary key (url)
);

--changeset garmoza:3
create table subscribe(
    tg_chat_id bigint,
    trackable_link_url text,
    last_update timestamp with time zone not null,

    primary key (tg_chat_id, trackable_link_url),
    foreign key (tg_chat_id) references tg_chat(id),
    foreign key (trackable_link_url) references trackable_link(url)
);
