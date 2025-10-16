
-- ==============================================
-- Mock Data Seed (EN) for 9 Tables (PostgreSQL)
-- - No inserts into auto-increment columns.
-- - Uses INSERT ... SELECT to resolve foreign keys via natural keys.
-- - Backfills profile.address_id after address insertion (1-1 link).
-- ==============================================

BEGIN;

-- 1) PROFILE (omit profile_id)
INSERT INTO profile (profile_name, profile_sname, profile_role, address_id, username, password, created_at)
VALUES
    ('Jirayu',   'Jaidee',   1, NULL, 'alice', 'alice123', NOW() - INTERVAL '14 days'),
    ('Warin',    'Sakulchai',1, NULL, 'bob',   'bob123',   NOW() - INTERVAL '10 days'),
    ('Pimchanok','Navachai', 2, NULL, 'carol', 'carol123', NOW() - INTERVAL '7 days');

-- 2) ADDRESS (omit address_id) — one-to-one with profile via profile_id
INSERT INTO address (phone, province, amphor, district, zip_code, addr_num, detail, received_name)
VALUES
    ('0812345678', 'Bangkok',     'Bang Kapi',     'Hua Mak',     '10240', '99/1', 'Near the university',  'Jirayu'),
    ('0891112222', 'Nonthaburi',  'Mueang',        'Talat Khwan', '11000', '12/45','Soi Wat Khae Nok',    'Warin');

-- Backfill profile.address_id from the inserted addresses (1-1 link)
UPDATE profile
SET address_id = 1
WHERE profile.username = 'alice';

UPDATE profile
SET address_id = 2
WHERE profile.username = 'bob';


-- 3) CATEGORY (omit category_id) — MUST include pizza, appetizer, drink
INSERT INTO category (category_name, category_img, category_product_path, category_priority)
VALUES
    ('pizza',     'pizza.png',     '/category/pizza',     1),
    ('appetizer', 'appetizer.png', '/category/appetizer', 2),
    ('drink',     'drink.png',     '/category/drink',     3);

-- 4) PRODUCT (omit product_id)
INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'BaconHam Cheese', 'Tomato sauce, mozzarella, basil', 'BaconHam_Cheese.png', 249, 10, 1, NOW() - INTERVAL '12 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'pizza';

INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'BBQ Smoked', 'Loaded pepperoni with stretchy cheese', 'BBQ_Smoked.png', 289, 5, 1, NOW() - INTERVAL '11 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'pizza';

INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'Double Pepperoni', 'Ham, pineapple, Loaded pepperoni', 'Double_Pepperoni.png', 269, 20, 1, NOW() - INTERVAL '10 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'pizza';

INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'Meat Deluxe', 'Tomato sauce, mozzarella', 'Meat_deluxe.png', 279, 2, 1, NOW() - INTERVAL '10 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'pizza';

INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'Spicy Chicken', 'pineapple', 'Spicy_Chicken.png', 299, 1, 1, NOW() - INTERVAL '10 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'pizza';



INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'Garlic Bread', 'Baked bread with garlic butter', 'garlic_bread.png', 79, 3, 1, NOW() - INTERVAL '9 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'appetizer';

INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'Chicken Wings', 'Crispy wings, mildly spicy', 'chicken_wings.png', 119, 6, 1, NOW() - INTERVAL '8 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'appetizer';

INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'Caesar Salad', 'Fresh lettuce, Caesar dressing, bacon bits', 'caesar_salad.png', 129, 6, 1, NOW() - INTERVAL '7 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'appetizer';

INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'Cola', 'Carbonated soft drink 330ml', 'cola.png', 35, 9, 1, NOW() - INTERVAL '6 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'drink';

INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'Lemon Tea', 'Sweet lemon iced tea 500ml', 'lemon_tea.png', 45, 5, 1, NOW() - INTERVAL '5 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'drink';

INSERT INTO product (category_id, product_name, product_detail, product_img, product_price, product_stock, is_active, created_at, created_by, updated_at, updated_by)
SELECT c.category_id, 'Water', 'Drinking water 600ml', 'water.png', 20, 0, 1, NOW() - INTERVAL '4 days', 'system', NULL, NULL
FROM category c WHERE c.category_name = 'drink';




-- 5) CART (omit cart_id)
INSERT INTO cart (profile_id, created_at, note)
SELECT p.profile_id, NOW() - INTERVAL '3 days', 'Alice cart #1'
FROM profile p WHERE p.username = 'alice';

INSERT INTO cart (profile_id, created_at, note)
SELECT p.profile_id, NOW() - INTERVAL '2 days', 'Bob cart #1'
FROM profile p WHERE p.username = 'bob';


-- 6) CART_ITEM (omit cart_item_id)
-- Alice: 1x BaconHam Cheese, 2x Cola
INSERT INTO cart_item (cart_id, product_id, qty, line_total)
SELECT c.cart_id, pr.product_id, 1, pr.product_price * 1
FROM cart c
         JOIN profile pf ON pf.username = 'alice' AND c.profile_id = pf.profile_id
         JOIN product pr ON pr.product_name = 'BaconHam Cheese';

INSERT INTO cart_item (cart_id, product_id, qty, line_total)
SELECT c.cart_id, pr.product_id, 2, pr.product_price * 2
FROM cart c
         JOIN profile pf ON pf.username = 'alice' AND c.profile_id = pf.profile_id
         JOIN product pr ON pr.product_name = 'Cola';

-- Bob: 1x BBQ Smoked, 1x Garlic Bread
INSERT INTO cart_item (cart_id, product_id, qty, line_total)
SELECT c.cart_id, pr.product_id, 1, pr.product_price * 1
FROM cart c
         JOIN profile pf ON pf.username = 'bob' AND c.profile_id = pf.profile_id
         JOIN product pr ON pr.product_name = 'BBQ Smoked';

INSERT INTO cart_item (cart_id, product_id, qty, line_total)
SELECT c.cart_id, pr.product_id, 1, pr.product_price * 1
FROM cart c
         JOIN profile pf ON pf.username = 'bob' AND c.profile_id = pf.profile_id
         JOIN product pr ON pr.product_name = 'Garlic Bread';


-- 7) ORDERS (omit order_id)  -- status example: 0=pending,1=fullfilled
INSERT INTO orders (profile_id, address_id, status, subtotal, delivery_fee, grand_total, created_at, fulfilled_at, fulfilled_by)
SELECT p.profile_id, p.address_id, 0, 249 + 2*35, 30, (249 + 2*35) + 30, NOW() - INTERVAL '2 days', NULL, NULL
FROM profile p WHERE p.username = 'alice';

INSERT INTO orders (profile_id, address_id, status, subtotal, delivery_fee, grand_total, created_at, fulfilled_at, fulfilled_by)
SELECT p.profile_id, p.address_id, 1, 289 + 79, 30, (289 + 79) + 30, NOW() - INTERVAL '1 day', NOW(), 'ops_user'
FROM profile p WHERE p.username = 'bob';

-- 8) ORDER_ITEM (omit order_item_id)
-- Alice order items
INSERT INTO order_item (order_id, product_id_snapshot, product_name, product_detail, product_price, qty, line_total)
SELECT o.order_id, 1, 'BaconHam Cheese','Tomato sauce, mozzarella, basil', 249, 1, 249
FROM orders o
         JOIN profile p ON p.username = 'alice' AND o.profile_id = p.profile_id
ORDER BY o.created_at DESC
    LIMIT 1;

INSERT INTO order_item (order_id, product_id_snapshot, product_name, product_detail, product_price, qty, line_total)
SELECT o.order_id, 9, 'Cola','Carbonated soft drink 330ml', 35, 2, 70
FROM orders o
         JOIN profile p ON p.username = 'alice' AND o.profile_id = p.profile_id
ORDER BY o.created_at DESC
    LIMIT 1;

-- Bob order items
INSERT INTO order_item (order_id, product_id_snapshot, product_name, product_detail, product_price, qty, line_total)
SELECT o.order_id, 2, 'BBQ Smoked','Loaded pepperoni with stretchy cheese', 289, 1, 289
FROM orders o
         JOIN profile p ON p.username = 'bob' AND o.profile_id = p.profile_id
ORDER BY o.created_at DESC
    LIMIT 1;

INSERT INTO order_item (order_id, product_id_snapshot, product_name, product_detail, product_price, qty, line_total)
SELECT o.order_id, 6, 'Garlic Bread','Baked bread with garlic butter', 79, 1, 79 * 1
FROM orders o
         JOIN profile p ON p.username = 'bob' AND o.profile_id = p.profile_id
ORDER BY o.created_at DESC
    LIMIT 1;

-- 9) RECOMMENDED_PRODUCT (omit recommended_id)
INSERT INTO recommended_product (product_id, recommended_img)
SELECT pr.product_id, 'BaconHam_Cheese.png'
FROM product pr WHERE pr.product_name = 'BaconHam Cheese';

INSERT INTO recommended_product (product_id, recommended_img)
SELECT pr.product_id, 'BBQ_Smoked.png'
FROM product pr WHERE pr.product_name = 'BBQ Smoked';

INSERT INTO recommended_product (product_id, recommended_img)
SELECT pr.product_id, 'lemon_tea.png'
FROM product pr WHERE pr.product_name = 'Lemon Tea';

COMMIT;
