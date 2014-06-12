# --- !Ups

create table course (
  id                        bigint not null,
  user_id                   integer,
  title                     varchar(255),
  web_data_ids              varchar(255),
  tags                      TEXT,
  constraint pk_course primary key (id))
;

create sequence course_seq;

# --- !Downs

drop table if exists course cascade;

drop sequence if exists course_seq;