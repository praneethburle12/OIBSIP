package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "issued_books")
public class IssuedBook {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @Column(nullable = false)
    private LocalDate issueDate;
    
    @Column(nullable = false)
    private LocalDate dueDate;
    
    private LocalDate returnDate;
    
    @Column(nullable = false)
    private String status; // ISSUED, RETURNED, OVERDUE
    
    private Double fineAmount = 0.0;
    
    private boolean finePaid = false;
    
    // Constructors
    public IssuedBook() {}
    
    public IssuedBook(User user, Book book, int allowedDays) {
        this.user = user;
        this.book = book;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(allowedDays);
        this.status = "ISSUED";
    }
    
    // Business Logic Methods
    public long getDaysOverdue() {
        if (returnDate != null) {
            return ChronoUnit.DAYS.between(dueDate, returnDate);
        } else {
            return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
    }
    
    public boolean isOverdue() {
        if (returnDate != null) {
            return returnDate.isAfter(dueDate);
        } else {
            return LocalDate.now().isAfter(dueDate);
        }
    }
    
    public void calculateFine(double finePerDay) {
        if (isOverdue()) {
            long overdueDays = getDaysOverdue();
            if (overdueDays > 0) {
                this.fineAmount = overdueDays * finePerDay;
            }
        }
    }
    
    public void returnBook(double finePerDay) {
        this.returnDate = LocalDate.now();
        calculateFine(finePerDay);
        this.status = isOverdue() ? "RETURNED_WITH_FINE" : "RETURNED";
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public LocalDate getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public boolean isFinePaid() {
        return finePaid;
    }
    
    public void setFinePaid(boolean finePaid) {
        this.finePaid = finePaid;
    }
}
