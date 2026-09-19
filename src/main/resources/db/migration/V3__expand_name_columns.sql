-- ЛР3 expand: добавляем first_name/last_name параллельно customer_full_name.

ALTER TABLE orders ADD COLUMN first_name VARCHAR(255);
ALTER TABLE orders ADD COLUMN last_name VARCHAR(255);

ALTER TABLE customers ADD COLUMN first_name VARCHAR(255);
ALTER TABLE customers ADD COLUMN last_name VARCHAR(255);
