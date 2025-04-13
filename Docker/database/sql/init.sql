create table client(
    id bigserial primary key,
    surname varchar(50) not null,
    name varchar(50) not null,
    patronymic varchar(50) not null,
    date_of_birth date not null,
    email varchar(255) not null,
    phone_number varchar(30) not null,
    password_hash varchar(255) not null,
    is_enabled boolean not null default true
);

create table passport(
    id bigserial primary key,
    number varchar(10) not null unique,
    surname varchar(50) not null,
    name varchar(50) not null,
    patronymic varchar(50) not null,
    date_of_birth date not null,
    gender varchar(10) not null,
    department varchar(255) not null,
    code_of_department varchar(255) not null,
    date_of_issue date not null,
    region varchar(255) not null,
    city varchar(255) not null,
    street varchar(255) not null,
    house_number varchar(255) not null,
    apartment_number int not null
);

create table documents(
    id bigserial primary key,
    client_id bigint references client(id),
    passport_id bigint references passport(id),
    itn varchar(12) unique
);

create table account(
    id bigserial primary key,
    account_name varchar(50) not null default 'Cчёт',
    deposit float(2) default 0,
    client_id bigint not null,
    type varchar(10) not null
);

create table transaction(
    id bigserial primary key,
    from_id bigint references account(id),
    from_name varchar(255) not null,
    to_id bigint references account(id),
    to_name varchar(255) not null,
    money float(2) not null,
    type varchar(50) not null,
    is_completed boolean not null,
    commited_at timestamp without time zone not null
);

create table business_client(
    id bigserial primary key,
    official_name varchar(255) not null,
    name varchar(255) not null
);

create table contact_person(
    id bigserial primary key,
    surname varchar(50) not null,
    name varchar(50) not null,
    patronymic varchar(50) not null,
    phone_number varchar(30) not null,
    email varchar(255) not null,
    business_client_id bigint references business_client(id)
);

create table card(
    id bigserial primary key,
    name varchar(50) not null,
    type varchar(50) not null,
    number varchar(16) not null,
    expiration_date date not null,
    account_id bigint references account(id),
    pin_hash varchar(255) not null,
    security_code varchar(3) not null
);