INSERT INTO categories (name, description, is_deleted)
VALUES
    ('Category 1', 'Test Category 1', false),
    ('Category 2', 'Test Category 2', false),
    ('Category 3', 'Test Category 3', false);

INSERT INTO books (title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    ('Test Book 1', 'Test Author 1', '1111111111111', 10.99, 'Test description', 'http://example.com/test-cover.jpg', false),
    ('Test Book 2', 'Test Author 2', '2222222222222', 15.99, 'Test description', 'http://example.com/test-cover.jpg', false),
    ('Test Book 3', 'Test Author 3', '3333333333333', 20.99, 'Test description', 'http://example.com/test-cover.jpg', false);

INSERT INTO books_categories (books_id, categories_id)
SELECT b.id, c.id FROM books b, categories c
WHERE (b.title = 'Test Book 1' AND c.name = 'Category 1')
   OR (b.title = 'Test Book 2' AND c.name = 'Category 1')
   OR (b.title = 'Test Book 3' AND c.name = 'Category 2');
