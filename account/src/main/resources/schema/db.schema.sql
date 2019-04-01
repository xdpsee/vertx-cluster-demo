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
