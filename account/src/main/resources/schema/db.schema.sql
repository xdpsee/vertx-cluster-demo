create table if not exists users
(
  id bigint primary key auto_increment,
  username varchar(32) not null,
  password varchar(128) not null,
  avatar varchar(255) default '',
  phone varchar(11) default '',
  email varchar(32) default '',
  create_at datetime not null,
  update_at timestamp default current_timestamp,
  constraint user_uk_1 unique (username),
  constraint user_uk_2 unique (phone)
);

create table if not exists roles
(
  id bigint primary key auto_increment,
  title varchar(32) not null,
  description varchar(255) default '',
  create_at datetime not null,
  update_at timestamp default current_timestamp,
  constraint role_uk_1 unique (title)
);

create table if not exists authorities
(
  id bigint primary key auto_increment,
  title varchar(32) not null,
  description varchar(255) default '',
  create_at datetime not null,
  update_at timestamp default current_timestamp,
  constraint authority_uk_1 unique (title)
);

create table if not exists user_roles(
  user_id bigint not null,
  role_id bigint not null,
  constraint user_role_uk_1 unique (user_id,role_id)
);

create table if not exists role_authorities(
  role_id bigint not null,
  authority_id bigint not null,
  constraint role_authority_uk_1 unique (role_id,authority_id)
);



