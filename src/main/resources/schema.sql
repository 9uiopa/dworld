create table IF NOT EXISTS boardtype
(
    id bigint auto_increment primary key,
    name varchar(255) not null
);

create table IF NOT EXISTS article
(
    id      int auto_increment
    primary key,
    title   varchar(50) not null,
    content text        null,
    author  varchar(20) not null,
    boardtype_id bigint not null ,
    created timestamp   null,
    updated timestamp   null,
    FOREIGN KEY (boardtype_id) REFERENCES boardtype(id)
    );


