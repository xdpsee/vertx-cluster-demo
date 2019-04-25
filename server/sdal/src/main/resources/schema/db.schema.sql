create table if not exists devices(
  `id` varchar(64) primary key not null,
  `create_at` timestamp not null,
  `update_at` timestamp default current_timestamp,
  `model` varchar(32) not null,
  `protocol` varchar(32) not null,
  `attributes` varchar(65535),
  `status` integer not null
);

create table if not exists positions (
  `id` bigint not null primary key auto_increment,
  `device_id` varchar(64) not null,
  `create_at` timestamp not null,
  `update_at` timestamp default current_timestamp,
  `time` timestamp  not null,
  `located` tinyint not null,
  `latitude` double not null,
  `longitude` double not null,
  `altitude` double not null,
  `speed` double not null,
  `course` double not null,
  `accuracy` double not null,
  `network` varchar(255),
  `attributes` varchar(65535),
  index position_idx_1(`device_id`, `located`, `time` desc)
);

create table if not exists events(
  `device_id` varchar(64) not null,
  `create_at` timestamp not null,
  `update_at` timestamp default current_timestamp,
  `time` timestamp not null,
  `type` integer not null,
  `position_id` bigint not null,
  `attributes` varchar(65535)
);

insert into devices(id,model,protocol,attributes,status,create_at, update_at) values (
  'IMEI-888888888888888',
  'mobile-test',
  'mobile',
  '{}',
  1,
  current_timestamp(),
  current_timestamp()
);