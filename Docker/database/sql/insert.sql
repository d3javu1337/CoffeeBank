insert into
    business_client(official_name, brand, email, password_hash)
values ('ООО Бнал', 'Арбузы на кутузе', 'mmm@cb.ru', '$2a$13$cSDq40MC87AwmqzkySNN9ecvGXrV/xzeUhkWNQKpHISURaw648yOG'),
       ('ООО Пойдёт щас возня', '10500 буквально на час', ' mmm2@cb.ru', '$2a$13$FCBKJ/L2aoouDrLkQw1//ue47oMOoW420luMDn2IfsVq8quJnD93y');

insert into
    payment_account(name, deposit, business_client_id, invoice_create_token)
values ('Расчётный счёт', 1337.77, 1, '3e22e8d8-492b-4676-b145-f0a14625f1eb'),
       ('Расчётный счёт', 228282.1, 2, '3cde6b11-0354-4cf6-b525-ba8fccae2e8d');

insert into
    invoice(id, amount, provider_payment_account_id)
values (gen_random_uuid(), 228, 1),
       (gen_random_uuid(), 13.37, 1),
       (gen_random_uuid(), 13372.28, 1);

insert into
    contact_person(surname, name, patronymic, phone_number, email, business_client_id)
values
    ('Иванов', 'Иван','Иванович', '88005553535', 'ivan@cb.ru', 1),
    ('Олегов', 'Олег','Олегович', '88005555555', 'oleg@cb.ru', 1),
    ('Бурмалдилов', 'Олег','Николаевич', '88228282133', 'burmalda@cb.ru', 2);

insert into
    admin(surname, name, patronymic, date_of_birth, email, phone_number, password_hash, is_enabled, role)
values (
        'adminov',
        'admin',
        'adminovich',
        '10-02-2000',
        'admin@cb.ru',
        '88888888888',
        '$2a$13$cSDq40MC87AwmqzkySNN9ecvGXrV/xzeUhkWNQKpHISURaw648yOG',
        true,
        'admin'
);