-- ЛР2: нормализация — customers + product_id в order_items.
-- customer_full_name в orders сохраняется для expand-contract миграции (ЛР3).

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    phone VARCHAR(50)
);

ALTER TABLE orders ADD COLUMN customer_id BIGINT REFERENCES customers(id);

INSERT INTO customers (full_name, address, phone)
SELECT DISTINCT customer_full_name, customer_address, customer_phone
FROM orders;

UPDATE orders o
SET customer_id = c.id
FROM customers c
WHERE c.full_name = o.customer_full_name
  AND COALESCE(c.address, '') = COALESCE(o.customer_address, '')
  AND COALESCE(c.phone, '') = COALESCE(o.customer_phone, '');

ALTER TABLE orders DROP COLUMN customer_address;
ALTER TABLE orders DROP COLUMN customer_phone;

-- Колонка customer_full_name сохраняется для expand-contract (ЛР3),
-- но больше не обязательна: данные клиента живут в customers.
ALTER TABLE orders ALTER COLUMN customer_full_name DROP NOT NULL;

ALTER TABLE order_items ADD COLUMN product_id BIGINT REFERENCES products(id);

UPDATE order_items oi
SET product_id = p.id
FROM products p
WHERE oi.product_name = p.name;

ALTER TABLE order_items DROP COLUMN product_name;
ALTER TABLE order_items DROP COLUMN product_price;

ALTER TABLE orders ALTER COLUMN customer_id SET NOT NULL;
ALTER TABLE order_items ALTER COLUMN product_id SET NOT NULL;
