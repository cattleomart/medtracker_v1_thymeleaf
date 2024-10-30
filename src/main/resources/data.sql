INSERT INTO USERMODEL (username, password, role)
SELECT 'admin@admin.com', 'admin', 'ADMIN'
WHERE
NOT EXISTS (
    SELECT username FROM USERMODEL WHERE username = 'admin@admin.com'
);