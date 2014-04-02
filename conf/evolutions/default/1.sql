# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table domains (
  domain                    varchar(255) not null,
  fav_icon_url              varchar(255),
  is_approved               boolean,
  constraint pk_domains primary key (domain))
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
  web_data_id               bigint not null,
  count                     integer,
  user_tags                 TEXT)
;

create table user_hash (
  hash                      varchar(255) not null,
  user_id                   bigint,
  constraint pk_user_hash primary key (hash))
;

create table web_data (
  id                        bigint not null,
  url                       varchar(255),
  title                     varchar(255),
  tags                      TEXT,
  words_count               integer,
  unique_words_count        integer,
  constraint pk_web_data primary key (id))
;

create sequence domains_seq;

create sequence users_seq;

create sequence user_data_seq;

create sequence user_hash_seq;

create sequence web_data_seq;




# --- !Downs

drop table if exists domains cascade;

drop table if exists query cascade;

drop table if exists users cascade;

drop table if exists user_data cascade;

drop table if exists user_hash cascade;

drop table if exists web_data cascade;

drop sequence if exists domains_seq;

drop sequence if exists users_seq;

drop sequence if exists user_data_seq;

drop sequence if exists user_hash_seq;

drop sequence if exists web_data_seq;

