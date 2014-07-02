# Add Bundle Drop Course

# --- !Ups

create table bundle (
  id                        bigint not null,
  user_id                   integer,
  description               varchar(255),
  title                     varchar(255),
  web_data_ids              varchar(255),
  tags                      TEXT,
  constraint pk_bundle primary key (id))
;




create sequence bundle_seq;