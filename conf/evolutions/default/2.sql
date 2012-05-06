# --- !Ups

create table item_order (
  id                        bigint not null,
  user_id                   bigint not null,
  order_status              integer,
  created_at                timestamp not null,
  updated_at                timestamp not null,
  constraint ck_item_order_order_status check (order_status in (0,1,2,3)),
  constraint pk_item_order primary key (id))
;

create table order_line_item (
  id                        bigint not null,
  order_id                  bigint,
  product_id                bigint,
  quantity                  bigint not null,
  price                     decimal(38) not null,
  created_at                timestamp not null,
  updated_at                timestamp not null,
  constraint pk_order_line_item primary key (id))
;

create sequence item_order_seq;

create sequence order_line_item_seq;

alter table item_order add constraint fk_item_order_user_6 foreign key (user_id) references account (id);
create index ix_item_order_user_6 on item_order (user_id);
alter table order_line_item add constraint fk_order_line_item_order_7 foreign key (order_id) references item_order (id);
create index ix_order_line_item_order_7 on order_line_item (order_id);
alter table order_line_item add constraint fk_order_line_item_product_8 foreign key (product_id) references product (id);
create index ix_order_line_item_product_8 on order_line_item (product_id);

# --- !Downs

drop table if exists item_order cascade;

drop table if exists order_line_item cascade;

drop sequence if exists item_order_seq;

drop sequence if exists order_line_item_seq;