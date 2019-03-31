create table if not exists todolists (
  id bigint primary key auto_increment,
  user_id bigint not null,
  title varchar(255) not null,
  status varchar(16) not null,
  create_at timestamp not null,
  update_at timestamp not null,
);
