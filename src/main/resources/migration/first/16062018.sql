CREATE TABLE first (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name CHARACTER VARYING NOT NULL,
  PRIMARY KEY (id)
);
insert into first(name) values('first');


create table WIRED
(
	ID BIGINT AUTO_INCREMENT not null
		primary key,
	name character varying not null,
	PARENT_ID BIGINT,
	constraint WIRED_FIRST_ID_FK
		foreign key (PARENT_ID) references FIRST
)
;

insert into WIRED (id,name,PARENT_ID) values (1,'first_wired',1);