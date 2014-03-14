# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table data (
  url                       varchar(255),
  tags                      varchar(255),
  title                     varchar(255),
  faviconurl                varchar(255),
  usertags                  varchar(255))
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

create sequence users_seq;




# --- !Downs

drop table if exists data cascade;

drop table if exists query cascade;

drop table if exists users cascade;

drop sequence if exists users_seq;

