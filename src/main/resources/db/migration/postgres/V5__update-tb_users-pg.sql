ALTER TABLE tb_users
ALTER COLUMN is_active TYPE BOOLEAN USING (is_active::boolean);