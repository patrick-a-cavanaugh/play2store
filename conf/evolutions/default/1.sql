# --- !Ups

create table cart (
  id                        bigint not null,
  user_id                   bigint,
  session_id                varchar(255),
  created_at                timestamp not null,
  updated_at                timestamp not null,
  constraint pk_cart primary key (id))
;

create table cart_line_item (
  id                        bigint not null,
  cart_id                   bigint,
  quantity                  bigint not null,
  created_at                timestamp not null,
  updated_at                timestamp not null,
  constraint pk_cart_line_item primary key (id))
;

create table category (
  id                        bigint not null,
  name                      varchar(255) not null,
  description               TEXT,
  parent_id                 bigint,
  image_id                  bigint,
  created_at                timestamp not null,
  updated_at                timestamp not null,
  constraint pk_category primary key (id))
;

create table image (
  id                        bigint not null,
  name                      varchar(255) not null,
  mime_type                 varchar(255) not null,
  data                      bytea,
  created_at                timestamp not null,
  updated_at                timestamp not null,
  constraint pk_image primary key (id))
;

create table product (
  id                        bigint not null,
  name                      varchar(255) not null,
  description               TEXT,
  price                     decimal(38) not null,
  category_id               bigint,
  image_id                  bigint,
  created_at                timestamp not null,
  updated_at                timestamp not null,
  constraint pk_product primary key (id))
;

create table "user" (
  id                        bigint not null,
  email                     varchar(255) not null,
  password_hash             varchar(255) not null,
  is_admin                  boolean DEFAULT FALSE not null,
  created_at                timestamp not null,
  updated_at                timestamp not null,
  constraint pk_user primary key (id))
;


create table product_image (
  product_id                     bigint not null,
  image_id                       bigint not null,
  constraint pk_product_image primary key (product_id, image_id))
;
create sequence cart_seq;

create sequence cart_line_item_seq;

create sequence category_seq;

create sequence image_seq;

create sequence product_seq;

create sequence user_seq;

alter table cart add constraint fk_cart_user_1 foreign key (user_id) references "user" (id);
create index ix_cart_user_1 on cart (user_id);
alter table cart_line_item add constraint fk_cart_line_item_cart_2 foreign key (cart_id) references cart (id);
create index ix_cart_line_item_cart_2 on cart_line_item (cart_id);
alter table category add constraint fk_category_parent_3 foreign key (parent_id) references category (id);
create index ix_category_parent_3 on category (parent_id);
alter table category add constraint fk_category_image_4 foreign key (image_id) references image (id);
create index ix_category_image_4 on category (image_id);
alter table product add constraint fk_product_category_5 foreign key (category_id) references category (id);
create index ix_product_category_5 on product (category_id);
alter table product add constraint fk_product_image_6 foreign key (image_id) references image (id);
create index ix_product_image_6 on product (image_id);



alter table product_image add constraint fk_product_image_product_01 foreign key (product_id) references product (id);

alter table product_image add constraint fk_product_image_image_02 foreign key (image_id) references image (id);

# --- !Downs

drop table if exists cart cascade;

drop table if exists cart_line_item cascade;

drop table if exists category cascade;

drop table if exists image cascade;

drop table if exists product cascade;

drop table if exists product_image cascade;

drop table if exists "user" cascade;

drop sequence if exists cart_seq;

drop sequence if exists cart_line_item_seq;

drop sequence if exists category_seq;

drop sequence if exists image_seq;

drop sequence if exists product_seq;

drop sequence if exists user_seq;

