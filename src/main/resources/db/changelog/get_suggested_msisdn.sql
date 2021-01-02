DROP PROCEDURE IF EXISTS get_suggested_msisdn;
DELIMITER $$
CREATE PROCEDURE get_suggested_msisdn(
IN_SEARCH_CRITERIA			VARCHAR(6), 	-- '-' FOR NOT SPECIFIED 
IN_APP_REGISTRATION_ID 		VARCHAR(255),	
IN_MSISDN_STATUS_AVAILABLE	BIGINT(20),		-- ID OF THE MSISDN STATUS 'AVAILABLE'
IN_MSISDN_STATUS_LOCKED		BIGINT(20),		-- ID OF THE MSISDN STATUS 'LOCKED'
IN_MSISDN_CATEGORY_ID_STR	VARCHAR(50),	-- COMMA SEPARATED MSISDN CATEGORY ID STRING e.g '1,2,3,4'
IN_EXPIRY_DURATION			INT,			-- TIME IN MINUTES TO LOCK THE NUMBERS
IN_SOURCE					VARCHAR(255),	-- APP/WEB
IN_LIMIT					INT				-- NUMBER OF RECORDS TO SHOW
)
BEGIN
 
DECLARE _fetch_counter 		INT DEFAULT 0;
DECLARE _stage 				INT DEFAULT 1;
DECLARE _limit 				INT;
DECLARE OUT_ERROR_CODE 		INTEGER;
DECLARE OUT_ERROR_TEXT 		VARCHAR(800);

DECLARE EXIT HANDLER FOR SQLEXCEPTION

	BEGIN

		GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE,
			@errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
		SELECT @errno, @text INTO OUT_ERROR_CODE, OUT_ERROR_TEXT;
		SELECT OUT_ERROR_CODE, OUT_ERROR_TEXT;
		ROLLBACK;
	END;

DROP TEMPORARY TABLE IF EXISTS `temp_gsm_msisdn`;
CREATE TEMPORARY TABLE `temp_gsm_msisdn` (
  `msisdn_id` 				BIGINT(20),
  `msisdn_number`			VARCHAR(255),
  `msisdn_category_id` 		BIGINT(20),
  `stage`					INT,
  `search_criteria`			VARCHAR(6),
  PRIMARY KEY (msisdn_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TEMPORARY TABLE IF EXISTS `temp_gsm_msisdn_final`;
CREATE TEMPORARY TABLE `temp_gsm_msisdn_final` (
  `msisdn_id` 				BIGINT(20),
  `msisdn_number`			VARCHAR(255),
  `msisdn_category_id` 		BIGINT(20),
  `stage`					INT,
  `search_criteria`			VARCHAR(6),
  PRIMARY KEY (msisdn_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET _limit = IN_LIMIT*2; -- SEARCHING DOUBLE INSTEAD OF REQUIRED IN ORDER TO COVER FOR THE POSSIBLE DISPLAYED MSISDN TO THE USER


-- FETCH THE FIRST SET OF NUMBERS
INSERT INTO temp_gsm_msisdn
SELECT 
	msisdn_id, 
    msisdn_number, 
    msisdn_category_id, 
    _stage, 
    IN_SEARCH_CRITERIA
FROM 
	msisdn
WHERE 
	msisdn_status = IN_MSISDN_STATUS_AVAILABLE
AND 
	FIND_IN_SET(msisdn_category_id,IN_MSISDN_CATEGORY_ID_STR)
AND 
	(IN_SEARCH_CRITERIA = '-' OR RIGHT(msisdn_number,6) REGEXP IN_SEARCH_CRITERIA)
ORDER BY 
	RAND() 
LIMIT 
	_limit 
;

-- DELETE ALREADY DISPLAYED NUMBERS
DELETE 
	m.*
FROM
	temp_gsm_msisdn m
    INNER JOIN msisdn_user_mapping mup ON m.msisdn_id = mup.msisdn_id
WHERE
	app_registration_id = IN_APP_REGISTRATION_ID
;

-- GET THE CURRENT RESULT COUNT
SELECT 
	COUNT(*)
INTO
	_fetch_counter
FROM
	temp_gsm_msisdn
;


WHILE _fetch_counter < IN_LIMIT AND LENGTH(IN_SEARCH_CRITERIA) > 1 DO

    SET IN_SEARCH_CRITERIA = RIGHT(IN_SEARCH_CRITERIA,LENGTH(IN_SEARCH_CRITERIA)-1);
    SET _stage = _stage +1;

	-- FETCH NEXT SET OF NUMBERS
	INSERT IGNORE INTO temp_gsm_msisdn
	SELECT 
		msisdn_id, 
		msisdn_number, 
        msisdn_category_id,
        _stage, 
        IN_SEARCH_CRITERIA
	FROM 
		msisdn
	WHERE 
		msisdn_status = IN_MSISDN_STATUS_AVAILABLE
	AND 
		FIND_IN_SET(msisdn_category_id,IN_MSISDN_CATEGORY_ID_STR)
	AND 
		RIGHT(msisdn_number,6) REGEXP IN_SEARCH_CRITERIA
	ORDER BY 
		RAND() 
	LIMIT 
		_limit
	;

	-- DELETE ALREADY DISPLAYED NUMBERS
	DELETE 
		m.*
	FROM
		temp_gsm_msisdn m
		INNER JOIN msisdn_user_mapping mup ON m.msisdn_id = mup.msisdn_id
	WHERE
		app_registration_id = IN_APP_REGISTRATION_ID
	;

	-- GET THE CURRENT RESULT COUNT
	SELECT 
		COUNT(*)
	INTO
		_fetch_counter
	FROM
		temp_gsm_msisdn
	;

END WHILE;

-- GET THE FINAL MSISDN LIST TO DISPLAY
INSERT INTO temp_gsm_msisdn_final
SELECT * FROM temp_gsm_msisdn ORDER BY stage, RAND() LIMIT IN_LIMIT;

-- UPDATE MSISDN_USER_MAPPING AND MSISDN FOR DISCARDED RECORDS
CALL update_discarded_and_selected_msisdn(IN_APP_REGISTRATION_ID,0,IN_MSISDN_STATUS_AVAILABLE,-1,0);

-- UPDATE MSISDN STATUS AS LOCKED
UPDATE
	msisdn m
    INNER JOIN temp_gsm_msisdn_final t ON m.msisdn_id = t.msisdn_id
SET
	m.msisdn_status = IN_MSISDN_STATUS_LOCKED
;

-- INSERT MSISDN_USER_MAPPING RECORDS
INSERT INTO msisdn_user_mapping (`msisdn_id`,`app_registration_id`,`expiry_time`,`source`,`is_display`,`modified_at`)
SELECT
	msisdn_id, IN_APP_REGISTRATION_ID, DATE_ADD(NOW(), INTERVAL IN_EXPIRY_DURATION MINUTE), IN_SOURCE, 0, NOW()
FROM
	temp_gsm_msisdn_final
;

UPDATE
	msisdn m
    INNER JOIN temp_gsm_msisdn_final t ON m.msisdn_id = t.msisdn_id
SET
	m.msisdn_status = IN_MSISDN_STATUS_LOCKED
;

SELECT * FROM temp_gsm_msisdn_final ORDER BY stage, msisdn_number;

DROP TEMPORARY TABLE IF EXISTS `temp_gsm_msisdn`;
DROP TEMPORARY TABLE IF EXISTS `temp_gsm_msisdn_final`;

END$$
DELIMITER ;



