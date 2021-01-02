DROP PROCEDURE IF EXISTS update_discarded_and_selected_msisdn;
DELIMITER $$
CREATE PROCEDURE update_discarded_and_selected_msisdn(
IN_APP_REGISTRATION_ID 		VARCHAR(255),
IN_SELETED_MSISDN_ID		BIGINT(20),		-- 0 FOR NOTHING SELECTED
IN_MSISDN_STATUS_AVAILABLE	BIGINT(20),		-- ID OF THE MSISDN STATUS 'AVAILABLE'
IN_MSISDN_STATUS_PICKED		BIGINT(20),		-- ID OF THE MSISDN STATUS 'PICKED'
IN_EXPIRY_DURATION			INT				-- TIME IN MINUTES TO LOCK THE SELECTED NUMBER
)
BEGIN
 
UPDATE
	msisdn m
    INNER JOIN msisdn_user_mapping mup ON m.msisdn_id = mup.msisdn_id
SET
	m.msisdn_status = IF(m.msisdn_id = IN_SELETED_MSISDN_ID, IN_MSISDN_STATUS_PICKED, IN_MSISDN_STATUS_AVAILABLE),
    mup.is_display = 1,
    mup.expiry_time = IF(m.msisdn_id = IN_SELETED_MSISDN_ID, 
							DATE_ADD(NOW(), INTERVAL IN_EXPIRY_DURATION MINUTE),
                            mup.expiry_time),
    mup.modified_at = NOW()
WHERE
	mup.app_registration_id = IN_APP_REGISTRATION_ID
AND
	mup.is_display = 0
;

-- GET THE EXPIRY TIME IN CASE OF SELECTION
IF(IN_SELETED_MSISDN_ID<>0) THEN
BEGIN

	SELECT 
		mup.expiry_time
	FROM
		msisdn m
		INNER JOIN msisdn_user_mapping mup ON m.msisdn_id = mup.msisdn_id
	WHERE
		mup.app_registration_id = IN_APP_REGISTRATION_ID
	AND
		m.msisdn_id = IN_SELETED_MSISDN_ID
	AND
		m.msisdn_status = IN_MSISDN_STATUS_PICKED
	;		

END;
END IF;


END$$
DELIMITER ;

