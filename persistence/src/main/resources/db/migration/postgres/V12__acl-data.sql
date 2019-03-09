INSERT INTO acl_sid (principal, sid) VALUES
    (1, 'PROJECT_MANAGER'),
    (1, 'DEVELOPER'),
    (0, 'TEXT_WRITER');

INSERT INTO acl_class (class) VALUES
    ('com.domclick.entity.acl.Answer');

INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
    (1, 1, NULL, 3, 0),
    (1, 2, NULL, 3, 0),
    (1, 3, NULL, 3, 0),
    (1, 4, NULL, 3, 0),
    (1, 5, NULL, 3, 0);


INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
    (1, 1, 1, 1, 1, 1, 1),
    (1, 2, 1, 2, 1, 1, 1),
    (5, 3, 1, 1, 1, 1, 1),
    (2, 1, 2, 1, 1, 1, 1),
    (5, 2, 2, 1, 1, 1, 1),
    (5, 4, 2, 2, 1, 1, 1),
    (2, 2, 3, 1, 1, 1, 1),
    (4, 3, 3, 1, 1, 1, 1);