package com.library.controller;

import com.library.model.*;
import com.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

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

    @GetMapping("")
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    // Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalBooks", bookService.countTotalBooks());
        model.addAttribute("availableBooks", bookService.countAvailableBooks());
        model.addAttribute("issuedBooks", issuedBookService.countTotalIssuedBooks());
        model.addAttribute("activeUsers", userService.countActiveUsers());
        model.addAttribute("pendingReservations", reservationService.countPendingReservations());
        model.addAttribute("pendingFines", issuedBookService.getTotalPendingFines());
        model.addAttribute("collectedFines", issuedBookService.getTotalCollectedFines());
        model.addAttribute("overdueBooks", issuedBookService.getOverdueBooks().size());
        return "admin/dashboard";
    }

    // Book Management
    @GetMapping("/books")
    public String listBooks(@RequestParam(required = false) String search, Model model) {
        List<Book> books;
        if (search != null && !search.isEmpty()) {
            books = bookService.searchBooks(search);
        } else {
            books = bookService.getAllBooks();
        }
        model.addAttribute("books", books);
        model.addAttribute("search", search);
        return "admin/books";
    }

    @GetMapping("/books/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/add-book";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book, @RequestParam String categoryName,
            RedirectAttributes redirectAttributes) {
        try {
            if (bookService.existsByIsbn(book.getIsbn())) {
                redirectAttributes.addFlashAttribute("error", "ISBN already exists");
                return "redirect:/admin/books/add";
            }
            Category category = categoryService.getOrCreateCategory(categoryName);
            book.setCategory(category);
            if (book.getAvailableQuantity() == null) {
                book.setAvailableQuantity(book.getTotalQuantity());
            }
            bookService.addBook(book);
            redirectAttributes.addFlashAttribute("success", "Book added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding book: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/edit-book";
    }

    @PostMapping("/books/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book, @RequestParam String categoryName,
            RedirectAttributes redirectAttributes) {
        try {
            Book existingBook = bookService.getBookById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            Category category = categoryService.getOrCreateCategory(categoryName);
            existingBook.setCategory(category);
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());

            // Adjust available quantity if total quantity changes
            int quantityDiff = book.getTotalQuantity() - existingBook.getTotalQuantity();
            existingBook.setTotalQuantity(book.getTotalQuantity());
            existingBook.setAvailableQuantity(existingBook.getAvailableQuantity() + quantityDiff);

            bookService.updateBook(existingBook);
            redirectAttributes.addFlashAttribute("success", "Book updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating book: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("success", "Book deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete book: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    // Category Management
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories";
    }

    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/add-category";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            if (categoryService.existsByName(category.getName())) {
                redirectAttributes.addFlashAttribute("error", "Category already exists");
                return "redirect:/admin/categories/add";
            }
            categoryService.addCategory(category);
            redirectAttributes.addFlashAttribute("success", "Category added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding category: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    // User Management
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getUsersByRole("USER"));
        return "admin/users";
    }

    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/add-user";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            if (userService.existsByUsername(user.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "Username already exists");
                return "redirect:/admin/users/add";
            }
            user.setRole("USER");
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "User added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/deactivate/{id}")
    public String deactivateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deactivateUser(id);
            redirectAttributes.addFlashAttribute("success", "User deactivated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deactivating user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // Issued Books Management
    @GetMapping("/issued-books")
    public String listIssuedBooks(Model model) {
        model.addAttribute("issuedBooks", issuedBookService.getAllIssuedBooks());
        model.addAttribute("overdueBooks", issuedBookService.getOverdueBooks());
        return "admin/issued-books";
    }

    // Reservations Management
    @GetMapping("/reservations")
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        model.addAttribute("pendingReservations", reservationService.getPendingReservations());
        return "admin/reservations";
    }

    @PostMapping("/reservations/approve/{id}")
    public String approveReservation(@PathVariable Long id,
            @RequestParam(required = false) String notes,
            RedirectAttributes redirectAttributes) {
        try {
            reservationService.approveReservation(id, notes);
            redirectAttributes.addFlashAttribute("success", "Reservation approved");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/reservations";
    }

    @PostMapping("/reservations/reject/{id}")
    public String rejectReservation(@PathVariable Long id,
            @RequestParam(required = false) String notes,
            RedirectAttributes redirectAttributes) {
        try {
            reservationService.rejectReservation(id, notes);
            redirectAttributes.addFlashAttribute("success", "Reservation rejected");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/reservations";
    }

    // Settings
    @GetMapping("/settings")
    public String showSettings(Model model) {
        model.addAttribute("settings", settingsService.getSettings());
        return "admin/settings";
    }

    @PostMapping("/settings")
    public String updateSettings(@ModelAttribute LibrarySettings settings, RedirectAttributes redirectAttributes) {
        try {
            settingsService.updateSettings(settings);
            redirectAttributes.addFlashAttribute("success", "Settings updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating settings: " + e.getMessage());
        }
        return "redirect:/admin/settings";
    }

    // Reports
    @GetMapping("/reports")
    public String showReports(Model model) {
        model.addAttribute("totalBooks", bookService.countTotalBooks());
        model.addAttribute("availableBooks", bookService.countAvailableBooks());
        model.addAttribute("issuedBooks", issuedBookService.countTotalIssuedBooks());
        model.addAttribute("overdueBooks", issuedBookService.getOverdueBooks());
        model.addAttribute("activeUsers", userService.countActiveUsers());
        model.addAttribute("pendingFines", issuedBookService.getTotalPendingFines());
        model.addAttribute("collectedFines", issuedBookService.getTotalCollectedFines());
        model.addAttribute("allIssuedBooks", issuedBookService.getAllIssuedBooks());
        return "admin/reports";
    }
}
