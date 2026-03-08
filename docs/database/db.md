schema (dbml):

```sql
Table client {
  id bigserial [primary key]
  surname varchar(50) [not null]
  name varchar(50) [not null]
  patronymic varchar(50) [not null]
  date_of_birth date [not null]
  email varchar(255) [not null, unique]
  phone_number varchar(30) [not null, unique]
  password_hash varchar(255) [not null]
  is_enabled boolean [not null, default: true]
}

Table passport {
  id bigserial [primary key]
  number varchar(10) [not null, unique]
  surname varchar(50) [not null]
  name varchar(50) [not null]
  patronymic varchar(50) [not null]
  date_of_birth date [not null]
  gender varchar(10) [not null]
  department varchar(255) [not null]
  code_of_department varchar(255) [not null]
  date_of_issue date [not null]
  region varchar(255) [not null]
  city varchar(255) [not null]
  street varchar(255) [not null]
  house_number varchar(255) [not null]
  apartment_number int [not null]
}

Table documents {
  id bigserial [primary key]
  client_id bigint [ref: > client.id]
  passport_id bigint [ref: > passport.id]
  itn varchar(12) [unique]
}

Table personal_account {
  id bigserial [primary key]
  name varchar(50) [not null]
  deposit float(2) [not null, default: 0]
  client_id bigint [not null, ref: > client.id]
  type varchar(10) [not null]
  indexes {
    (client_id, type) [unique]
  }
}

Table transaction {
  id uuid [primary key]
  sender_id bigint [not null, ref: > personal_account.id]
  recipient_id bigint [ref: > personal_account.id]
  amount float(2) [not null]
  type varchar(50) [not null]
  is_completed boolean [not null]
  commited_at timestamp [not null]
}

Table business_client {
  id bigserial [primary key]
  official_name varchar(255) [not null]
  brand varchar(255) [not null]
  email varchar(255) [not null, unique]
  password_hash varchar(255) [not null]
}

Table contact_person {
  id bigserial [primary key]
  surname varchar(50) [not null]
  name varchar(50) [not null]
  patronymic varchar(50) [not null]
  phone_number varchar(30) [not null]
  email varchar(255) [not null]
  business_client_id bigint [ref: > business_client.id]
}

Table payment_account {
  id bigserial [primary key]
  name varchar(50) [not null]
  deposit float(2) [not null, default: 0]
  business_client_id bigint [not null, ref: > business_client.id, unique]
  invoice_create_token uuid [unique]
}

Table card {
  id bigserial [primary key]
  name varchar(50) [not null]
  type varchar(50) [not null]
  number varchar(16) [not null]
  expiration_date date [not null]
  account_id bigint [ref: > personal_account.id]
  pin_hash varchar(255)
  security_code varchar(3) [not null]
}

Table invoice {
  id uuid [primary key]
  amount float(2) [not null]
  provider_payment_account_id bigint [not null, ref: > payment_account.id]
}

Table payment {
  id uuid [primary key]
  payment_account_id bigint [not null, ref: > payment_account.id]
  personal_account_id bigint [not null, ref: > personal_account.id]
  transaction_id uuid [not null, ref: > transaction.id]
  invoice_id uuid [not null, ref: > invoice.id, unique]
}
```