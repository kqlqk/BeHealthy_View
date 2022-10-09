drop table if exists auth_info;

create table auth_info
(
    id            bigint       not null unique auto_increment,
    user_id       bigint       not null,
    access_token  varchar(200) not null,
    refresh_token varchar(200) not null,
    remote_addr   varchar(100) not null unique,

    primary key (id)
);
