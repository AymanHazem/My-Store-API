INSERT INTO categories (name)
VALUES ('Fruits & Vegetables'),
       ('Dairy'),
       ('Bakery'),
       ('Beverages'),
       ('Snacks');

INSERT INTO products (name, price, description, category_id)
VALUES ('Bananas', 1.99, 'Fresh organic bananas, sold per pound.', 1),
       ('Tomatoes', 2.49, 'Red vine tomatoes, great for salads.', 1),
       ('Whole Milk', 3.25, '1 gallon of whole milk from local farm.', 2),
       ('Cheddar Cheese', 4.50, 'Aged cheddar cheese block, 200g.', 2),
       ('Sourdough Bread', 3.75, 'Artisan sourdough loaf baked daily.', 3),
       ('Croissants', 5.20, 'Pack of 6 buttery French croissants.', 3),
       ('Orange Juice', 3.99, '100% pure orange juice, 1 liter.', 4),
       ('Iced Tea', 2.50, 'Sweetened iced tea bottle, 500ml.', 4),
       ('Potato Chips', 2.25, 'Salted potato chips, 150g pack.', 5),
       ('Chocolate Cookies', 3.00, 'Soft chocolate chip cookies, pack of 10.', 5);


