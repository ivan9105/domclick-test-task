CREATE TABLE BANK_COMPANY (
   ID          SERIAL PRIMARY KEY,
   NAME        VARCHAR(255) NOT NULL,
   VERSION     BIGINT NOT NULL
);

CREATE TABLE BANK_DEPARTMENT (
   ID          SERIAL PRIMARY KEY,
   NAME        VARCHAR(255) NOT NULL,
   VERSION     BIGINT NOT NULL,
   COMPANY_ID  BIGINT NOT NULL REFERENCES BANK_COMPANY(ID) ON DELETE CASCADE
);

INSERT INTO BANK_COMPANY(NAME, VERSION) VALUES
    ('Pepsi', 1),
    ('Coca Cola', 1),
    ('Sprite', 1);

INSERT INTO BANK_DEPARTMENT(NAME, VERSION, COMPANY_ID) VALUES
    ('Sales & Marketing', 1, 1),
    ('Research & Development', 1, 1),
    ('Administration ', 1, 1),
    ('Human Resources', 1, 2),
    ('Sales & Marketing', 1, 3);