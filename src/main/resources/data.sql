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
SELECT 'patient@pat.com', '$2a$10$WSU4n.NhUE7g1lwMAeTT9OXAaGJG2s.4UkhYIYuIcT0qn0AxNV8NO', 'USER'
WHERE
NOT EXISTS (
    SELECT username FROM USERMODEL WHERE username = 'patient@pat.com'
);
-- admin password in plaintext is 'abc', for now admin role has no special privileges