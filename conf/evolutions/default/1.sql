# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table webData (
  id                        bigint not null,
  url                       varchar(255),
  tags                      TEXT,
  title                     varchar(255),
  words                     integer,
  unique_words              integer,
  constraint pk_data primary key (id))
;

create table domains (
  id                        integer not null,
  domain                    varchar(255),
  faviconurl                varchar(255),
  is_approved               boolean,
  constraint pk_domains primary key (id))
;

create table query (
  connected                 varchar(255),
  query                     varchar(255))
;

create table users (
  email                     varchar(255) not null,
  password                  varchar(255),
  constraint pk_users primary key (email))
;

create table user_data (
  user_id                   integer not null,
  data_id                   bigint not null,
  count                     integer,
  user_tags                 TEXT)
;

create sequence data_seq;

create sequence domains_seq;

create sequence users_seq;

create sequence user_data_seq;




# --- !Downs

drop table if exists webData cascade;

drop table if exists domains cascade;

drop table if exists query cascade;

drop table if exists users cascade;

drop table if exists user_data cascade;

drop sequence if exists data_seq;

drop sequence if exists domains_seq;

drop sequence if exists users_seq;

drop sequence if exists user_data_seq;

