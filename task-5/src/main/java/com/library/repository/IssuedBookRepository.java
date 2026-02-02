package com.library.repository;

import com.library.model.Book;
import com.library.model.IssuedBook;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IssuedBookRepository extends JpaRepository<IssuedBook, Long> {

    List<IssuedBook> findByUser(User user);

    List<IssuedBook> findByBook(Book book);

    List<IssuedBook> findByStatus(String status);

    List<IssuedBook> findByUserAndStatus(User user, String status);

    @Query("SELECT ib FROM IssuedBook ib WHERE ib.status = 'ISSUED' AND ib.dueDate < :currentDate")
    List<IssuedBook> findOverdueBooks(LocalDate currentDate);

    @Query("SELECT COUNT(ib) FROM IssuedBook ib WHERE ib.user = :user AND ib.status = 'ISSUED'")
    Long countActiveIssuesByUser(User user);

    @Query("SELECT COUNT(ib) FROM IssuedBook ib WHERE ib.status = 'ISSUED'")
    Long countTotalIssuedBooks();

    @Query("SELECT SUM(ib.fineAmount) FROM IssuedBook ib WHERE ib.finePaid = false AND ib.fineAmount > 0")
    Double getTotalPendingFines();

    @Query("SELECT SUM(ib.fineAmount) FROM IssuedBook ib WHERE ib.finePaid = true")
    Double getTotalCollectedFines();
}
