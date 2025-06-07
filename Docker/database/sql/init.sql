create table client(
    id bigserial primary key,
    surname varchar(50) not null,
    name varchar(50) not null,
    patronymic varchar(50) not null,
    date_of_birth date not null,
    email varchar(255) unique not null,
    phone_number varchar(30) unique not null,
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

create table personal_account(
    id bigserial primary key,
    name varchar(50) not null,
    deposit float(2) default 0 not null,
    client_id bigint references client(id) not null,
    type varchar(10) not null,
    unique(client_id, type)
);

create table transaction(
    id uuid primary key,
    sender_id bigint references personal_account(id) not null ,
    recipient_id bigint references personal_account(id),
    amount float(2) not null,
    type varchar(50) not null,
    is_completed boolean not null,
    commited_at timestamp without time zone not null
);

create table business_client(
    id bigserial primary key,
    official_name varchar(255) not null,
    brand varchar(255) not null,
    email varchar(255) unique not null,
    password_hash varchar(255) not null
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

create table payment_account(
    id bigserial primary key,
    name varchar(50) not null,
    deposit float(2) default 0 not null ,
    business_client_id bigint references business_client(id) not null unique ,
);

create table card(
    id bigserial primary key,
    name varchar(50) not null,
    type varchar(50) not null,
    number varchar(16) not null,
    expiration_date date not null,
    account_id bigint references personal_account(id),
    pin_hash varchar(255),
    security_code varchar(3) not null
);


create table invoice(
    id uuid primary key,
    amount float(2) not null,
    provider_payment_account_id bigint references payment_account(id) not null
);

create table payment(
    id uuid primary key,
    payment_account_id bigint references payment_account(id) not null,
    personal_account_id bigint references personal_account(id) not null,
    transaction_id uuid references transaction(id) not null,
    invoice_id uuid references invoice(id) not null unique
);
