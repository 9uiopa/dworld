create table IF NOT EXISTS article
(
    id      int auto_increment
    primary key,
    title   varchar(50) not null,
    content text        null,
    author  varchar(20) not null,
    created timestamp   null,
    updated timestamp   null
    );
