create type roles as enum (
    'BASE_CLIENT',
    'BASE_SUPPORT',
    'BUSINESS_CLIENT',
    'BUSINESS_CONTACT_PERSON',
    'BUSINESS_SUPPORT'
    );

create sequence cb_id_seq start 1;

create or replace function to_cb_id(id bigint) returns text as $$
declare
    symbols text := '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    result text := '';
    n bigint := id;
    remainder int;
begin
    --            if id = 0 then
--               return '000000';
--            end if;
    while n > 0 loop
            remainder := n % 36;
            result := substring(symbols from remainder + 1 for 1) || result;
            n := n / 36;
        end loop;
    return lpad(result, 6, '0');
end;
$$ language plpgsql immutable;

create or REPLACE function generate_cb_id() returns varchar(9) as $$
BEGIN
    RETURN 'cb-' || to_cb_id(nextval('cb_id_seq'));
END;
$$ LANGUAGE plpgsql;

create DOMAIN cb_id as VARCHAR(9)
    default generate_cb_id() constraint valid_cb_id check(
    VALUE ~ '^cb-[0-9A-Z]{6}$'
    );

create table client_auth_data(
                                 id cb_id primary key,
                                 email varchar(255) unique not null,
                                 phone_number varchar(30) unique not null,
                                 password_hash varchar(255) not null,
                                 is_enabled boolean not null default false
);

create table client_roles(
                             client_id cb_id references client_auth_data(id),
                             role roles not null
);

create type outbox_registration_type as enum(
    'BASE',
    'BUSINESS'
    );

create table registration_outbox(
                                    client_id varchar(9) not null unique,
                                    payload jsonb not null,
                                    type outbox_registration_type not null,
                                    creation_time timestamp not null default now(),
                                    next_retry_time timestamp not null default now()
);

create table session(
                        id text primary key,
                        opened_at timestamp not null default now(),
                        last_interaction_time timestamp not null default now(),
                        user_agent text not null,
                        access_token text not null,
                        refresh_token text not null
);

create table sessions(
                         id text not null,
                         session_id text references session(id)
);