create database library;
use library;

#drop database library;

create table author (
	author_id bigint primary key unique not null auto_increment,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    full_name varchar(101) unique,
    birth_city varchar(100),
    birth_country varchar(100),
    birth_date date,
    description varchar(1000),
    nationality varchar(30)
);

create table genre (
	genre_id bigint primary key not null auto_increment unique,
    genre_name varchar(50) not null,
    genre_description varchar(1000)
);

create table book (
    book_id bigint primary key unique not null auto_increment,
    book_name varchar(255) not null,
    book_language varchar(50) not null,
    book_description varchar(1000),
    book_height double precision,
    book_length double precision,
    book_width double precision,
    pages_count integer,
    publication_year integer,

    author_id bigint not null,
    genre_id bigint not null,

    foreign key(author_id) references author(author_id) on delete cascade,
    foreign key(genre_id) references genre(genre_id) on delete cascade
);