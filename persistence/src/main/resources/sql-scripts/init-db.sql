INSERT INTO applications (id, name, created_at, updated_at, deleted) VALUES ('1', 'instapic', '2020-04-11T17:41:20+0000', '2020-04-11T17:41:20+0000', false);
INSERT INTO end_users (id, client, created_at, updated_at, deleted, application_id) VALUES ('1', '975f2f10-7c1b-11ea-bc55-0242ac130003', '2020-04-11T17:41:20+0000', '2020-04-11T17:41:20+0000', false, '1');
INSERT INTO flags (id, value, name, description, created_at, updated_at, deleted, application_id, datatype, defaultvalue) VALUES ('1', 1, 'Upload quality', 'Manage server load', '2020-04-11T17:41:20+0000', '2020-04-11T17:41:20+0000', false, '1', 'BOOL', true);
INSERT INTO applications_flags (application_id, flags_id) VALUES ('1', '1');
INSERT INTO rules (id, created_at, updated_at, deleted, application_id, flag_id, enduser_id) VALUES ('1', '2020-04-11T17:41:20+0000', '2020-04-11T17:41:20+0000', false, '1', '1', '1');
