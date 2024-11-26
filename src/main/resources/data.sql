INSERT INTO USERMODEL (username, password, role)
SELECT 'admin@admin.com', '$2a$10$WSU4n.NhUE7g1lwMAeTT9OXAaGJG2s.4UkhYIYuIcT0qn0AxNV8NO', 'ADMIN'
WHERE
NOT EXISTS (
    SELECT username FROM USERMODEL WHERE username = 'admin@admin.com'
);

INSERT INTO USERMODEL (username, password, role)
SELECT 'doc@doc.com', '$2a$10$WSU4n.NhUE7g1lwMAeTT9OXAaGJG2s.4UkhYIYuIcT0qn0AxNV8NO', 'PRACT'
WHERE
NOT EXISTS (
    SELECT username FROM USERMODEL WHERE username = 'doc@doc.com'
);

INSERT INTO USERMODEL (username, password, role)
SELECT 'patient1@pat.com', '$2a$10$WSU4n.NhUE7g1lwMAeTT9OXAaGJG2s.4UkhYIYuIcT0qn0AxNV8NO', 'USER'
WHERE
NOT EXISTS (
    SELECT username FROM USERMODEL WHERE username = 'patient1@pat.com'
);
INSERT INTO USERMODEL (username, password, role)
SELECT 'patient2@pat.com', '$2a$10$WSU4n.NhUE7g1lwMAeTT9OXAaGJG2s.4UkhYIYuIcT0qn0AxNV8NO', 'USER'
WHERE
NOT EXISTS (
    SELECT username FROM USERMODEL WHERE username = 'patient2@pat.com'
);

INSERT INTO PRACTITIONERROLEREQUEST (USERMODEL_ID, APPROVED)
SELECT '2', 'TRUE'
WHERE
NOT EXISTS (
    SELECT USERMODEL_ID FROM PRACTITIONERROLEREQUEST WHERE USERMODEL_ID = '2'
);

INSERT INTO PRESCRIPTION (ID, DRUG, PATIENT_ID, PRACTITIONER_ID, BEGIN_TIME, END_TIME, DOSE_MG)
SELECT '1','FOO', '3', '2', {ts '2022-04-22 10:34:53.44'}, {ts '2022-04-22 10:34:53.44'}, '15'
WHERE
NOT EXISTS (
    SELECT PATIENT_ID FROM PRESCRIPTION WHERE PATIENT_ID = '3'
);


INSERT INTO DAILYEVALUATION (USERMODEL_ID, RECORD_DATE)
SELECT '1', {ts '2024-04-22'}
WHERE
NOT EXISTS (
    SELECT USERMODEL_ID FROM DAILYEVALUATION WHERE USERMODEL_ID = '1'
);

INSERT INTO MEDICATION (ID, NAME)
SELECT '1', 'FOO_MED'
WHERE
NOT EXISTS (
    SELECT ID FROM MEDICATION WHERE ID = '1'
);

INSERT INTO PRESCRIPTIONSCHEDULEENTRY (DAY_STAGE, PRESCRIPTION_ID)
SELECT 'BREAKFAST', '1'
WHERE
NOT EXISTS (
    SELECT PRESCRIPTION_ID FROM PRESCRIPTIONSCHEDULEENTRY WHERE PRESCRIPTION_ID = '1' AND DAY_STAGE = 'BREAKFAST'
);
INSERT INTO PRESCRIPTIONSCHEDULEENTRY (DAY_STAGE, PRESCRIPTION_ID)
SELECT 'LUNCH', '1'
WHERE
NOT EXISTS (
    SELECT PRESCRIPTION_ID FROM PRESCRIPTIONSCHEDULEENTRY WHERE PRESCRIPTION_ID = '1' AND DAY_STAGE = 'LUNCH'
);

-- all password in plaintext are 'abc'