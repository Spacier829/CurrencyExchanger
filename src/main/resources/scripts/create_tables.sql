CREATE TABLE IF NOT EXISTS currencies
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    code      VARCHAR(3) CHECK (length(code) == 3) UNIQUE NOT NULL,
    full_name VARCHAR(60) UNIQUE                          NOT NULL,
    sign      VARCHAR(3)                                  NOT NULL
    );

CREATE TABLE IF NOT EXISTS exchange_rates
(
    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id   INTEGER FOREIGN KEY REFERENCES currencies,
    target_currency_id INTEGER FOREIGN KEY REFERENCES currencies,
    rate               DECIMAL(6) NOT NULL,
    CONSTRAINT unique_currencies_id UNIQUE (base_currency_id, target_currency_id),
    CONSTRAINT not_equal_currencies CHECK ( base_currency_id != target_currency_id)
    );