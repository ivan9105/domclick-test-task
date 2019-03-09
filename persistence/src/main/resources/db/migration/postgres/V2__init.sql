CREATE TABLE BANK_USER (
   ID           SERIAL PRIMARY KEY,
   FIRST_NAME   VARCHAR(255) NOT NULL,
   LAST_NAME    VARCHAR(255) NOT NULL,
   MIDDLE_NAME  VARCHAR(255) NOT NULL,
   VERSION      BIGINT NOT NULL
);

CREATE TABLE BANK_ACCOUNT (
   ID           SERIAL PRIMARY KEY,
   BALANCE      DECIMAL(20, 2) NOT NULL,
   USER_ID      BIGINT NOT NULL REFERENCES BANK_USER(ID),
   VERSION      BIGINT NOT NULL
);

INSERT INTO BANK_USER (FIRST_NAME, LAST_NAME, MIDDLE_NAME, VERSION) VALUES 
    ('Иван', 'Мельников', 'Игоревич', 0),
    ('Мария', 'Злобина', 'Петровна', 0),
    ('Сергей', 'Игнатов', 'Андреевич', 0),
    ('Петр', 'Федоров', 'Сергеевич', 0),
    ('Руслан', 'Злобин', 'Петрович', 0),
    ('Маргарита', 'Парфенова', 'Олеговна', 0);

INSERT INTO BANK_ACCOUNT (BALANCE, USER_ID, VERSION) VALUES 
    (15.50, 1, 0),
    (100000.30, 1, 0),
    (50000.25, 2, 0),
    (3000.12, 3, 0),
    (125.0, 4, 0),
    (623.0, 5, 0),
    (550.0, 6, 0),
    (110.0, 6, 0),
    (100.0, 6, 0);