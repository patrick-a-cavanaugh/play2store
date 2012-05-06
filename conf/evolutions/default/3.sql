# --- !Ups

create table address (
  id                        bigint not null,
  line1                     varchar(255) not null,
  line2                     varchar(255) not null,
  city                      varchar(255) not null,
  state                     varchar(255) not null,
  postal_code               varchar(31) not null,
  country_code              varchar(3) not null,
  created_at                timestamp not null,
  updated_at                timestamp not null,
  constraint pk_address primary key (id));

create sequence address_seq;

alter table item_order add column shipping_address_id bigint not null;
alter table item_order add column billing_address_id bigint not null;

alter table item_order add constraint fk_item_order_shipping_address
  foreign key (shipping_address_id) references address (id);
alter table item_order add constraint fk_item_order_billing_address
  foreign key (billing_address_id) references address (id);

# --- !Downs

alter table item_order
  drop constraint fk_item_order_shipping_address,
  drop constraint fk_item_order_billing_address;

alter table item_order
  drop column shipping_address restrict,
  drop column billing_address restrict;

drop table if exists address;

drop sequence if exists address_seq;