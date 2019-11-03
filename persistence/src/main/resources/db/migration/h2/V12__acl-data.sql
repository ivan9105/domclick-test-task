INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 1, 'PROJECT_MANAGER'),
(2, 1, 'DEVELOPER'),
(3, 0, 'TEXT_WRITER');

INSERT INTO acl_class (id, class) VALUES
(1, 'com.domclick.entity.AnswerEntity');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 3, 0),
(2, 1, 2, NULL, 3, 0),
(3, 1, 3, NULL, 3, 0),
(4, 1, 4, NULL, 3, 0),
(5, 1, 5, NULL, 3, 0);


MERGE INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 1, 1, 1, 1);

MERGE INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(2, 1, 2, 1, 2, 1, 1, 1);

MERGE INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(3, 5, 3, 1, 1, 1, 1, 1);

MERGE INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(4, 2, 1, 2, 1, 1, 1, 1);

MERGE INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(5, 5, 2, 2, 1, 1, 1, 1);

MERGE INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(6, 5, 4, 2, 2, 1, 1, 1);

MERGE INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(7, 2, 2, 3, 1, 1, 1, 1);

MERGE INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(8, 4, 3, 3, 1, 1, 1, 1);