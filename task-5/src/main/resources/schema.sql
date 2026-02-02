-- Digital Library Management System Database Schema
-- MySQL Script

-- Create Database (Handled by connection URL)
-- CREATE DATABASE IF NOT EXISTS modern_library_db;
-- USE modern_library_db;

-- Drop existing tables (COMMENTED OUT TO PERSIST USER DATA)
-- DROP TABLE IF EXISTS issued_books;
-- DROP TABLE IF EXISTS reservations;
-- DROP TABLE IF EXISTS books;
-- DROP TABLE IF EXISTS categories;
-- DROP TABLE IF EXISTS users;
-- DROP TABLE IF EXISTS library_settings;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL, -- ADMIN or USER
    active BOOLEAN DEFAULT TRUE,
    registered_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Categories Table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Books Table
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    category_id BIGINT NOT NULL,
    total_quantity INT NOT NULL DEFAULT 1,
    available_quantity INT NOT NULL DEFAULT 1,
    publisher VARCHAR(255),
    publication_year INT,
    description TEXT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    INDEX idx_title (title),
    INDEX idx_author (author),
    INDEX idx_isbn (isbn),
    INDEX idx_category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Issued Books Table
CREATE TABLE IF NOT EXISTS issued_books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(30) NOT NULL, -- ISSUED, RETURNED, OVERDUE, RETURNED_WITH_FINE
    fine_amount DOUBLE DEFAULT 0.0,
    fine_paid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE RESTRICT,
    INDEX idx_user (user_id),
    INDEX idx_book (book_id),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Reservations Table
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    reservation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30) NOT NULL, -- PENDING, APPROVED, REJECTED, FULFILLED, CANCELLED
    approval_date DATETIME,
    admin_notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_book (book_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Library Settings Table
CREATE TABLE IF NOT EXISTS library_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    allowed_days INT NOT NULL DEFAULT 14,
    fine_per_day DOUBLE NOT NULL DEFAULT 5.0,
    max_books_per_user INT NOT NULL DEFAULT 3
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Default data now handled by DataInitializer.java
-- Create Views for Reporting

-- Available Books View
CREATE OR REPLACE VIEW v_available_books AS
SELECT b.id, b.title, b.author, b.isbn, c.name as category, 
       b.available_quantity, b.total_quantity
FROM books b
JOIN categories c ON b.category_id = c.id
WHERE b.available_quantity > 0;

-- Currently Issued Books View
CREATE OR REPLACE VIEW v_currently_issued AS
SELECT ib.id, u.full_name as user_name, u.email, 
       b.title as book_title, b.author, b.isbn,
       ib.issue_date, ib.due_date, 
       DATEDIFF(CURDATE(), ib.due_date) as days_overdue,
       ib.fine_amount
FROM issued_books ib
JOIN users u ON ib.user_id = u.id
JOIN books b ON ib.book_id = b.id
WHERE ib.status = 'ISSUED';

-- Overdue Books View
CREATE OR REPLACE VIEW v_overdue_books AS
SELECT ib.id, u.full_name as user_name, u.email, u.phone,
       b.title as book_title, b.isbn,
       ib.issue_date, ib.due_date,
       DATEDIFF(CURDATE(), ib.due_date) as days_overdue
FROM issued_books ib
JOIN users u ON ib.user_id = u.id
JOIN books b ON ib.book_id = b.id
WHERE ib.status = 'ISSUED' AND ib.due_date < CURDATE();

-- Pending Reservations View
CREATE OR REPLACE VIEW v_pending_reservations AS
SELECT r.id, u.full_name as user_name, u.email,
       b.title as book_title, b.author,
       r.reservation_date
FROM reservations r
JOIN users u ON r.user_id = u.id
JOIN books b ON r.book_id = b.id
WHERE r.status = 'PENDING'
ORDER BY r.reservation_date ASC;

-- Fine Collection Report View
CREATE OR REPLACE VIEW v_fine_report AS
SELECT 
    COUNT(*) as total_fines,
    SUM(CASE WHEN fine_paid = TRUE THEN fine_amount ELSE 0 END) as collected_fines,
    SUM(CASE WHEN fine_paid = FALSE THEN fine_amount ELSE 0 END) as pending_fines,
    SUM(fine_amount) as total_fine_amount
FROM issued_books
WHERE fine_amount > 0;

-- Contact Queries Table (New)
CREATE TABLE IF NOT EXISTS contact_queries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    user_id BIGINT,
    status VARCHAR(20) DEFAULT 'NEW', -- NEW, READ, REPLIED
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user_query (user_id),
    INDEX idx_status_query (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Contact Queries View (New)
CREATE OR REPLACE VIEW v_contact_queries AS
SELECT cq.id, u.full_name as user_name, u.email, cq.subject, cq.message, cq.status, cq.created_at
FROM contact_queries cq
LEFT JOIN users u ON cq.user_id = u.id;

-- End of Script
