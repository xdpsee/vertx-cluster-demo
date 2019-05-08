create table if not exists users
(
  id bigint primary key auto_increment,
  parent_id bigint not null,
  username varchar(32) not null,
  password varchar(128) not null,
  avatar varchar(255) default '',
  phone varchar(11) default '',
  email varchar(32) default '',
  create_at timestamp not null,
  update_at timestamp default current_timestamp,
  constraint user_uk_1 unique (username),
  constraint user_uk_2 unique (phone)
);

create table if not exists user_roles
(
  user_id bigint not null,
  role_id bigint not null,
  constraint user_role_uk_1 unique (user_id,role_id)
);

create table if not exists role_authorities
(
  role_id bigint not null,
  authority_id bigint not null,
  constraint role_authority_uk_1 unique (role_id,authority_id)
);



create table if not exists user_groups
(
  id bigint primary key auto_increment,
  user_id bigint not null,
  title varchar(64) not null,
  create_at timestamp not null,
  update_at timestamp default current_timestamp,
  constraint user_group_uk_1 unique (user_id,title)
);

create table if not exists user_group_members
(
  group_id bigint not null,
  user_id bigint not null,
  create_at timestamp not null,
  update_at timestamp default current_timestamp,
  constraint user_group_member_uk_1 unique (group_id,user_id)
);



create table if not exists user_group_permissions
(
  group_id bigint not null,
  permission_id bigint not null,
  create_at timestamp not null,
  update_at timestamp default current_timestamp,
  constraint user_group_permission_uk_1 unique (group_id,permission_id)
);


create table if not exists device_groups
(
  id bigint primary key auto_increment,
  user_id bigint not null,
  title varchar(64) not null,
  create_at timestamp not null,
  update_at timestamp default current_timestamp,
  constraint device_group_uk_1 unique (user_id,title)
);

create table if not exists device_group_members
(
  group_id bigint not null,
  device_id varchar(64) not null,
  create_at timestamp not null,
  update_at timestamp default current_timestamp,
  constraint device_group_member_uk_1 unique (group_id,device_id)
);


create table if not exists user_device_binds
(
  user_id bigint not null,
  device_id varchar(64) not null,
  create_at timestamp not null,
  update_at timestamp default current_timestamp,
  constraint user_device_bind_uk_1 unique (user_id,device_id)
);