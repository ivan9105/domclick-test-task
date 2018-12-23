CREATE TABLE IF NOT EXISTS BANK_ANSWER
(
   ID BIGINT auto_increment,
   CONTENT VARCHAR(1024) NOT NULL,
   VERSION BIGINT NOT NULL,
   PRIMARY KEY(ID)
);

MERGE INTO BANK_ANSWER (ID, CONTENT, VERSION) VALUES (1, 'Гордо встану грудью за спиной Украины.', 0);
MERGE INTO BANK_ANSWER (ID, CONTENT, VERSION) VALUES (2, 'Я не вижу запаха.', 0);
MERGE INTO BANK_ANSWER (ID, CONTENT, VERSION) VALUES (3, 'Я не разбрасываюсь словами: мне потом их трудно подбирать…', 0);
MERGE INTO BANK_ANSWER (ID, CONTENT, VERSION) VALUES (4, 'Я захожу там, где готов заходить.', 0);
MERGE INTO BANK_ANSWER (ID, CONTENT, VERSION) VALUES (5, 'Если у человека стабильная психика, не стоит нагружать ее походами к психотерапевтам.', 0);