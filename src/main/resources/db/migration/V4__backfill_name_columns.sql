-- ЛР3 backfill: заполняем first_name/last_name из customer_full_name / full_name.

UPDATE orders
SET first_name = SPLIT_PART(customer_full_name, ' ', 1),
    last_name = CASE
        WHEN POSITION(' ' IN customer_full_name) > 0
        THEN SUBSTRING(customer_full_name FROM POSITION(' ' IN customer_full_name) + 1)
        ELSE ''
    END;

UPDATE customers
SET first_name = SPLIT_PART(full_name, ' ', 1),
    last_name = CASE
        WHEN POSITION(' ' IN full_name) > 0
        THEN SUBSTRING(full_name FROM POSITION(' ' IN full_name) + 1)
        ELSE ''
    END;

ALTER TABLE orders ALTER COLUMN first_name SET NOT NULL;
ALTER TABLE orders ALTER COLUMN last_name SET NOT NULL;
ALTER TABLE customers ALTER COLUMN first_name SET NOT NULL;
ALTER TABLE customers ALTER COLUMN last_name SET NOT NULL;
