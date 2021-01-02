CREATE TABLE `msisdn_user_mapping_aud` (
  hist_id INT AUTO_INCREMENT PRIMARY KEY,
  `id` bigint NOT NULL ,
  `msisdn_id` bigint NOT NULL,
  `app_registration_id` varchar(255) NOT NULL,
  `expiry_time` timestamp NOT NULL,
  `source` varchar(255) DEFAULT NULL,
  `is_display` bit(1) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `modified_by` bigint DEFAULT NULL,
  `modified_at` timestamp NULL DEFAULT NULL,
  action VARCHAR(50) DEFAULT NULL
);

CREATE TRIGGER msisdn_user_mapping_insert_trigger
    AFTER INSERT ON msisdn_user_mapping
    FOR EACH ROW 
 INSERT INTO msisdn_user_mapping_aud
 SET action = 'INSERT',
  id=NEW.msisdn_user_mapping_id,
  msisdn_id=NEW.msisdn_id,
  app_registration_id=NEW.app_registration_id,
  expiry_time=NEW.expiry_time,
  source=NEW.source,
  is_display=NEW.is_display,
  created_by=NEW.created_by,
  created_at=NEW.created_at,
  modified_by=NEW.modified_by,
  modified_at=NEW.modified_at;

CREATE TRIGGER msisdn_user_mapping_update_trigger
    AFTER UPDATE ON msisdn_user_mapping
    FOR EACH ROW 
 INSERT INTO msisdn_user_mapping_aud
 SET action = 'UPDATE',
  id=OLD.msisdn_user_mapping_id,
  msisdn_id=OLD.msisdn_id,
  app_registration_id=OLD.app_registration_id,
  expiry_time=OLD.expiry_time,
  source=OLD.source,
  is_display=OLD.is_display,
  created_by=OLD.created_by,
  created_at=OLD.created_at,
  modified_by=OLD.modified_by,
  modified_at=OLD.modified_at;
  
 CREATE TRIGGER msisdn_user_mapping_delete_trigger
    BEFORE DELETE ON msisdn_user_mapping
    FOR EACH ROW 
 INSERT INTO msisdn_user_mapping_aud
 SET action = 'DELETE',
  id=OLD.msisdn_user_mapping_id,
  msisdn_id=OLD.msisdn_id,
  app_registration_id=OLD.app_registration_id,
  expiry_time=OLD.expiry_time,
  source=OLD.source,
  is_display=OLD.is_display,
  created_by=OLD.created_by,
  created_at=OLD.created_at,
  modified_by=OLD.modified_by,
  modified_at=OLD.modified_at;