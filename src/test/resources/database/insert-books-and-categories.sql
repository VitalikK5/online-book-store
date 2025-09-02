INSERT INTO categories (id, name, description, is_deleted)
VALUES
    (1, 'Category 1', 'Test Category 1', false),
    (2, 'Category 2', 'Test Category 2', false),
    (3, 'Category 3', 'Test Category 3', false);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    (1, 'Test Book 1', 'Test Author 1', '1111111111111', 10.99, 'Test description', 'http://example.com/test-cover.jpg', false),
    (2, 'Test Book 2', 'Test Author 2', '2222222222222', 15.99, 'Test description', 'http://example.com/test-cover.jpg', false),
    (3, 'Test Book 3', 'Test Author 3', '3333333333333', 20.99, 'Test description', 'http://example.com/test-cover.jpg', false);

INSERT INTO books_categories (books_id, categories_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 2);
