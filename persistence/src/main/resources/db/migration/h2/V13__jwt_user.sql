CREATE TABLE IF NOT EXISTS JWT_USER
(
   ID BIGINT auto_increment,
   USERNAME VARCHAR(255) NOT NULL,
   PASSWORD VARCHAR(255) NOT NULL,
   VERSION  BIGINT NOT NULL,
   PRIMARY KEY(ID),
   UNIQUE KEY unique_jwt_user_username (USERNAME)
);

INSERT INTO JWT_USER (USERNAME, PASSWORD, VERSION) VALUES ('ivan9105','$2a$04$fQPhH6cPuCKQcnVc.jk5b.0uXxTt45dDdtMtFrwTGI5sd1SeiKhsu', 1);