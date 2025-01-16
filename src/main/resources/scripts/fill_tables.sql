INSERT INTO currencies (code, full_name, sign)
VALUES ('AUD', 'Australian dollar', 'A$'),
       ('USD', 'United States dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('RUB', 'Russian Ruble', '₽'),
       ('JPY', 'Yen', '¥'),
       ('GBP', 'Pound Sterling', '£'),
       ('KZT', 'Tenge', '₸');

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
VALUES (2, 3, 106.1),
       (7, 3, 102.81),
       (3, 6, 5.1576);
