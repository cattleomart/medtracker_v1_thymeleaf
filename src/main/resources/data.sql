INSERT INTO USERMODEL (username, password, role)
SELECT 'admin@admin.com', '$2a$10$WSU4n.NhUE7g1lwMAeTT9OXAaGJG2s.4UkhYIYuIcT0qn0AxNV8NO', 'ADMIN'
WHERE
NOT EXISTS (
    SELECT username FROM USERMODEL WHERE username = 'admin@admin.com'
);
-- admin password in plaintext is 'abc', for now admin role has no special privileges