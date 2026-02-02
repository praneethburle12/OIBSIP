package com.library.controller;

import com.library.model.*;
import com.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private IssuedBookService issuedBookService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LibrarySettingsService settingsService;

    @Autowired
    private ContactQueryService contactQueryService;

    // Dashboard
    @GetMapping("/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<IssuedBook> activeIssues = issuedBookService.getActiveIssuedBooksByUser(user);
        List<Reservation> userReservations = reservationService.getReservationsByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("activeIssues", activeIssues);
        model.addAttribute("reservations", userReservations);
        model.addAttribute("availableBooks", bookService.getAvailableBooks().size());

        // Calculate total fines
        double totalFines = activeIssues.stream()
                .filter(IssuedBook::isOverdue)
                .mapToDouble(ib -> {
                    ib.calculateFine(settingsService.getSettings().getFinePerDay());
                    return ib.getFineAmount();
                })
                .sum();
        model.addAttribute("totalFines", totalFines);

        return "user/dashboard";
    }

    // Browse Books
    @GetMapping("/books")
    public String browseBooks(@RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            Model model) {
        List<Book> books;

        if (categoryId != null && categoryId > 0) {
            Category category = categoryService.getCategoryById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            if (search != null && !search.isEmpty()) {
                books = bookService.searchBooksByCategory(search, category);
            } else {
                books = bookService.getBooksByCategory(category);
            }
        } else if (search != null && !search.isEmpty()) {
            books = bookService.searchBooks(search);
        } else {
            books = bookService.getAllBooks();
        }

        model.addAttribute("books", books);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", categoryId);

        return "user/books";
    }

    // View Book Details
    @GetMapping("/books/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        model.addAttribute("book", book);
        return "user/book-details";
    }

    // Issue Book
    @PostMapping("/books/issue/{id}")
    public String issueBook(@PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Book book = bookService.getBookById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            issuedBookService.issueBook(user, book);
            redirectAttributes.addFlashAttribute("success", "Book issued successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/user/dashboard";
    }

    // Reserve Book
    @PostMapping("/books/reserve/{id}")
    public String reserveBook(@PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Book book = bookService.getBookById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            reservationService.createReservation(user, book);
            redirectAttributes.addFlashAttribute("success", "Book reserved successfully! Wait for admin approval.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/user/books";
    }

    // My Issued Books
    @GetMapping("/my-books")
    public String myBooks(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<IssuedBook> issuedBooks = issuedBookService.getIssuedBooksByUser(user);
        LibrarySettings settings = settingsService.getSettings();

        // Calculate fines
        issuedBooks.forEach(ib -> {
            if (ib.getReturnDate() == null && ib.isOverdue()) {
                ib.calculateFine(settings.getFinePerDay());
            }
        });

        model.addAttribute("issuedBooks", issuedBooks);
        model.addAttribute("settings", settings);

        return "user/my-books";
    }

    // Return Book (User view - shows due date and fine)
    @GetMapping("/books/return/{id}")
    public String showReturnPage(@PathVariable Long id, Model model) {
        IssuedBook issuedBook = issuedBookService.getIssuedBookById(id)
                .orElseThrow(() -> new RuntimeException("Issue record not found"));

        LibrarySettings settings = settingsService.getSettings();
        if (issuedBook.isOverdue()) {
            issuedBook.calculateFine(settings.getFinePerDay());
        }

        model.addAttribute("issuedBook", issuedBook);
        model.addAttribute("settings", settings);

        return "user/return-book";
    }

    @PostMapping("/books/return/{id}")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            IssuedBook issuedBook = issuedBookService.returnBook(id);

            if (issuedBook.getFineAmount() > 0) {
                redirectAttributes.addFlashAttribute("warning",
                        "Book returned with fine: ₹" + issuedBook.getFineAmount());
            } else {
                redirectAttributes.addFlashAttribute("success", "Book returned successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/user/my-books";
    }

    // My Reservations
    @GetMapping("/my-reservations")
    public String myReservations(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Reservation> reservations = reservationService.getReservationsByUser(user);
        model.addAttribute("reservations", reservations);

        return "user/my-reservations";
    }

    // Cancel Reservation
    @PostMapping("/reservations/cancel/{id}")
    public String cancelReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(id);
            redirectAttributes.addFlashAttribute("success", "Reservation cancelled successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/user/my-reservations";
    }

    // Contact Us / Query
    @GetMapping("/contact")
    public String showContactPage(Model model) {
        return "user/contact";
    }

    @PostMapping("/contact")
    public String submitQuery(@RequestParam String subject,
            @RequestParam String message,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ContactQuery query = new ContactQuery();
            query.setSubject(subject);
            query.setMessage(message);
            query.setUser(user);
            query.setStatus("NEW");

            contactQueryService.saveQuery(query);

            redirectAttributes.addFlashAttribute("success",
                    "Your query has been sent to the librarian! We will get back to you soon.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/user/contact";
    }
}
