# --- !Ups

create table bundle (
  id                        bigint not null,
  user_id                   integer,
  description               varchar(255),
  title                     varchar(255),
  web_data_ids              varchar(255),
  tags                      TEXT,
  constraint pk_course primary key (id))
;

create sequence bundle_seq;


# --- !Downs

drop table if exists course cascade;

drop sequence if exists course_seq;

drop table if exists bundle cascade;

drop sequence if exists bundle_seq;

