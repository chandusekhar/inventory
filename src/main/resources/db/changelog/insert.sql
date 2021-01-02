INSERT INTO msisdn_status (msisdn_status_name, is_active) SELECT 'Available',1;
INSERT INTO msisdn_status (msisdn_status_name, is_active) SELECT 'Ready for recycling',1;
INSERT INTO msisdn_status (msisdn_status_name, is_active) SELECT 'In use',1;
INSERT INTO msisdn_status (msisdn_status_name, is_active) SELECT 'Pre-use',1;
INSERT INTO msisdn_status (msisdn_status_name, is_active) SELECT 'Picked2',1;
INSERT INTO msisdn_status (msisdn_status_name, is_active) SELECT 'Picked1',1;
INSERT INTO msisdn_status (msisdn_status_name, is_active) SELECT 'Purchased',1;
INSERT INTO msisdn_status (msisdn_status_name, is_active) SELECT 'Ordered',1;
INSERT INTO msisdn_status (msisdn_status_name, is_active) SELECT 'Locked',1;

insert into msisdn_category(msisdn_category_name,is_active) values ('FREE',1);
insert into msisdn_category(msisdn_category_name,is_active) values ('SILVER',1);
insert into msisdn_category(msisdn_category_name,is_active) values ('BRONZE',1);
insert into msisdn_category(msisdn_category_name,is_active) values ('GOLD',1);

