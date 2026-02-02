package com.library.repository;

import com.library.model.Book;
import com.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByCategory(Category category);

    List<Book> findByAvailableQuantityGreaterThan(int quantity);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(String keyword);

    @Query("SELECT b FROM Book b WHERE b.category = :category AND (" +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Book> searchBooksByCategory(String keyword, Category category);

    @Query("SELECT COUNT(b) FROM Book b")
    Long countTotalBooks();

    @Query("SELECT SUM(b.availableQuantity) FROM Book b")
    Long countAvailableBooks();

    boolean existsByIsbn(String isbn);
}
