CREATE TABLE BANK_ANSWER (
   ID           SERIAL PRIMARY KEY,
   CONTENT      VARCHAR(1024) NOT NULL,
   VERSION      BIGINT NOT NULL
);

INSERT INTO  BANK_ANSWER (CONTENT, VERSION) VALUES
    ('Гордо встану грудью за спиной Украины.', 0),
    ('Я не вижу запаха.', 0),
    ('Я не разбрасываюсь словами: мне потом их трудно подбирать…', 0),
    ('Я захожу там, где готов заходить.', 0),
    ('Если у человека стабильная психика, не стоит нагружать ее походами к психотерапевтам.', 0);