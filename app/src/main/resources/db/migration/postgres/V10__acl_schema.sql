CREATE TABLE acl_sid (
  id                   SERIAL PRIMARY KEY,
  principal            SMALLINT NOT NULL,
  sid                  VARCHAR(100) NOT NULL,
  UNIQUE               (sid,principal)
);

CREATE TABLE acl_class (
  id                   SERIAL PRIMARY KEY,
  class                VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE acl_object_identity (
  id                   SERIAL PRIMARY KEY,
  object_id_class      BIGINT NOT NULL REFERENCES acl_class (id),
  object_id_identity   BIGINT NOT NULL,
  parent_object        BIGINT DEFAULT NULL,
  owner_sid            BIGINT DEFAULT NULL REFERENCES acl_sid (id),
  entries_inheriting   SMALLINT NOT NULL,
  UNIQUE               (object_id_class,object_id_identity)
);

CREATE TABLE acl_entry (
  id                   SERIAL PRIMARY KEY,
  acl_object_identity  BIGINT NOT NULL REFERENCES acl_object_identity(id),
  ace_order            INT NOT NULL,
  sid                  BIGINT NOT NULL REFERENCES acl_sid(id),
  mask                 INT NOT NULL,
  granting             SMALLINT NOT NULL,
  audit_success        SMALLINT NOT NULL,
  audit_failure        SMALLINT NOT NULL,
  UNIQUE               (acl_object_identity,ace_order)
);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);