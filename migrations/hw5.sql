--liquibase formatted sql

--changeset garmoza:4
ALTER TABLE trackable_link
ADD last_crawl timestamp with time zone not null default now();
