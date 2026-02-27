
INSERT INTO supplier (id, name, address, contact_number, email)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Sony Electronics', 'Tokyo', '+81-3-6748-2111', 'contact@sony.com'),
    ('22222222-2222-2222-2222-222222222222', 'Logitech', 'Switzerland', '+41-21-863-5511', 'b2b@logitech.com'),
    ('33333333-3333-3333-3333-333333333333', 'Whole Foods', 'Texas', '+1-512-477-4455', 'purchasing@wholefoods.com')
    ON CONFLICT (id) DO NOTHING;  -- <--- THE MAGIC LINE

INSERT INTO warehouse (id, name, location, max_capacity, current_quantity)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Central', 'New York', 5000, 150),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'West Coast', 'Seattle', 1000, 20)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO product (id, name, sku, type, price, min_stock, supplier_id)
VALUES
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'PS5', 'SONY-PS5', 'ELECTRONICS', 499.99, 10, '11111111-1111-1111-1111-111111111111'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Sony TV', 'SONY-TV', 'ELECTRONICS', 799.50, 5, '11111111-1111-1111-1111-111111111111'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Mouse', 'LOGI-MX', 'ELECTRONICS', 99.99, 20, '22222222-2222-2222-2222-222222222222'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Avocados', 'FOOD-AVO', 'FOOD', 1.49, 100, '33333333-3333-3333-3333-333333333333')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO stock_entries (id, product_id, warehouse_id, quantity)
VALUES
    ('12345678-1234-1234-1234-1234567890ab', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 50),
    ('12345678-1234-1234-1234-1234567890ac', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 100),
    ('12345678-1234-1234-1234-1234567890ad', 'ffffffff-ffff-ffff-ffff-ffffffffffff', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 20)
    ON CONFLICT (id) DO NOTHING;