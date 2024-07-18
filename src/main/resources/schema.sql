create table IF NOT EXISTS board_type
(
    id bigint auto_increment primary key,
    name varchar(255) not null
);
ALTER TABLE board_type ADD CONSTRAINT unique_name UNIQUE (name);


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


