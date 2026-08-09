INSERT INTO products (sku, name, category, price, stock_quantity) VALUES
('ELEC-1001', 'Logitech MX Master 3S Mouse', 'Electronics', 99.99, 15),
('ELEC-1002', 'Dell UltraSharp 27-inch 4K Monitor', 'Electronics', 449.50, 4),
('ELEC-1003', 'Mechanical RGB Keyboard', 'Electronics', 120.00, 2),
('OFF-2001', 'Ergonomic Mesh Office Chair', 'Furniture', 249.99, 8),
('OFF-2002', 'Electric Standing Desk', 'Furniture', 399.00, 12)
ON DUPLICATE KEY UPDATE id=id;