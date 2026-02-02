package com.library.service;

import com.library.model.Book;
import com.library.model.IssuedBook;
import com.library.model.LibrarySettings;
import com.library.model.User;
import com.library.repository.IssuedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IssuedBookService {

    @Autowired
    private IssuedBookRepository issuedBookRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private LibrarySettingsService settingsService;

    public List<IssuedBook> getAllIssuedBooks() {
        return issuedBookRepository.findAll();
    }

    public List<IssuedBook> getIssuedBooksByUser(User user) {
        return issuedBookRepository.findByUser(user);
    }

    public List<IssuedBook> getActiveIssuedBooksByUser(User user) {
        return issuedBookRepository.findByUserAndStatus(user, "ISSUED");
    }

    public List<IssuedBook> getOverdueBooks() {
        return issuedBookRepository.findOverdueBooks(LocalDate.now());
    }

    public Optional<IssuedBook> getIssuedBookById(Long id) {
        return issuedBookRepository.findById(id);
    }

    public IssuedBook issueBook(User user, Book book) throws Exception {
        LibrarySettings settings = settingsService.getSettings();

        // Check if book is available
        if (book.getAvailableQuantity() <= 0) {
            throw new Exception("Book is not available");
        }

        // Check user's active issues
        long activeIssues = issuedBookRepository.countActiveIssuesByUser(user);
        if (activeIssues >= settings.getMaxBooksPerUser()) {
            throw new Exception("Maximum book limit reached");
        }

        // Create issue record
        IssuedBook issuedBook = new IssuedBook(user, book, settings.getAllowedDays());
        issuedBook = issuedBookRepository.save(issuedBook);

        // Decrease book availability
        bookService.decreaseAvailability(book);

        return issuedBook;
    }

    public IssuedBook returnBook(Long issuedBookId) throws Exception {
        Optional<IssuedBook> issuedBookOpt = issuedBookRepository.findById(issuedBookId);

        if (issuedBookOpt.isEmpty()) {
            throw new Exception("Issued book record not found");
        }

        IssuedBook issuedBook = issuedBookOpt.get();

        if (issuedBook.getReturnDate() != null) {
            throw new Exception("Book already returned");
        }

        LibrarySettings settings = settingsService.getSettings();

        // Process return
        issuedBook.returnBook(settings.getFinePerDay());
        issuedBook = issuedBookRepository.save(issuedBook);

        // Increase book availability
        bookService.increaseAvailability(issuedBook.getBook());

        return issuedBook;
    }

    public void payFine(Long issuedBookId) throws Exception {
        Optional<IssuedBook> issuedBookOpt = issuedBookRepository.findById(issuedBookId);

        if (issuedBookOpt.isEmpty()) {
            throw new Exception("Issued book record not found");
        }

        IssuedBook issuedBook = issuedBookOpt.get();
        issuedBook.setFinePaid(true);
        issuedBookRepository.save(issuedBook);
    }

    public long countTotalIssuedBooks() {
        Long count = issuedBookRepository.countTotalIssuedBooks();
        return count != null ? count : 0L;
    }

    public Double getTotalPendingFines() {
        Double fines = issuedBookRepository.getTotalPendingFines();
        return fines != null ? fines : 0.0;
    }

    public Double getTotalCollectedFines() {
        Double fines = issuedBookRepository.getTotalCollectedFines();
        return fines != null ? fines : 0.0;
    }
}
