create database library;
use library;

#drop database library;

create table author (
	id bigint primary key not null auto_increment,
    author_id bigint unique not null,
    first_name varchar(50) not null,
    second_name varchar(50) not null,
    birth_city varchar(100),
    birth_country varchar(100),
    birth_date date,
    author_descr varchar(1000),
    nationality varchar(30)
);

create table genre (
	id bigint primary key not null auto_increment,
	genre_id bigint unique not null,
    genre_name varchar(50) not null,
    genre_descr varchar(1000)
);

create table book (
	id bigint primary key not null auto_increment, 
    book_id bigint unique not null,
    book_name varchar(255) not null,
    book_language varchar(50) not null,
    book_descr varchar(1000),
    book_height double precision,
    book_length double precision,
    book_width double precision,
    page_count integer,
    publication_year integer,
    
    author_id bigint not null,
    genre_id bigint not null,
    
    foreign key(author_id) references author(author_id) on delete cascade,
    foreign key(genre_id) references genre(genre_id) on delete cascade
);