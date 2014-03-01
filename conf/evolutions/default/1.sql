# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table data (
  url                       varchar(255) not null,
  tags                      varchar(255),
  constraint pk_data primary key (url))
;

create table users (
  email                     varchar(255) not null,
  password                  varchar(255),
  constraint pk_users primary key (email))
;

create sequence data_seq;

create sequence users_seq;




# --- !Downs

drop table if exists data cascade;

drop table if exists users cascade;

drop sequence if exists data_seq;

drop sequence if exists users_seq;

