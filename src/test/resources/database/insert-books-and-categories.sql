-- Drop tables if they exist (H2-compatible)
DROP TABLE IF EXISTS books_categories;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS categories;

-- Recreate tables (simplified schema for test env only)
CREATE TABLE categories (
                            id BIGINT PRIMARY KEY,
                            name VARCHAR(255),
                            description VARCHAR(255),
                            is_deleted BOOLEAN
);

CREATE TABLE books (
                       id BIGINT PRIMARY KEY,
                       title VARCHAR(255),
                       author VARCHAR(255),
                       isbn VARCHAR(13),
                       price DECIMAL(10,2),
                       description VARCHAR(255),
                       cover_image VARCHAR(255),
                       is_deleted BOOLEAN
);

CREATE TABLE books_categories (
                                  books_id BIGINT,
                                  categories_id BIGINT
);

-- Insert sample data
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
