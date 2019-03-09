CREATE TABLE BANK_COMPANY (
   ID          SERIAL PRIMARY KEY,
   NAME        VARCHAR(255) NOT NULL
);

CREATE TABLE BANK_DEPARTMENT (
   ID          SERIAL PRIMARY KEY,
   NAME        VARCHAR(255) NOT NULL,
   COMPANY_ID  BIGINT NOT NULL REFERENCES BANK_COMPANY(ID)
);

INSERT INTO BANK_COMPANY(NAME) VALUES 
    ('Pepsi'), 
    ('Coca Cola'), 
    ('Sprite');

INSERT INTO BANK_DEPARTMENT(NAME, COMPANY_ID) VALUES
    ('Sales & Marketing', 1),
    ('Research & Development', 1),
    ('Administration ', 1),
    ('Human Resources', 2),
    ('Sales & Marketing', 3);