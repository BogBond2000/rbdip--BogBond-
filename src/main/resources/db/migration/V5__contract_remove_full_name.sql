-- ЛР3 contract: удаляем устаревшие колонки full_name / customer_full_name.

ALTER TABLE orders DROP COLUMN customer_full_name;
ALTER TABLE orders DROP COLUMN first_name;
ALTER TABLE orders DROP COLUMN last_name;
ALTER TABLE customers DROP COLUMN full_name;
