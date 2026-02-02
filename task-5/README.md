# 📚 Modern Digital Library

A complete, full-stack web-based Digital Library Management System built with **Spring Boot**, **MySQL**, **Thymeleaf**, and **Bootstrap**. This system automates all core library operations including book management, issuing, returning, fines calculation, advance booking (reservations), and comprehensive reporting.

---

## 🎯 Project Overview

This is an **internship-level project** that demonstrates enterprise-grade software development with:
- **Role-based authentication** (Admin & User)
- **Complete CRUD operations** on books, users, and categories
- **Business logic** for book issuing, returning, and fine calculation
- **Database-driven architecture** with JPA/Hibernate
- **Modern, responsive UI** with Bootstrap 5
- **RESTful design patterns**

---

## ✨ Key Features

### 🔐 Authentication & Authorization
- Secure login/registration system
- Role-based access control (Admin vs User)
- Spring Security integration with BCrypt password encoding

### 👨‍💼 Admin Module
Administrators have full control:
- ✅ **Book Management**: Add, edit, delete, and search books
- ✅ **Category Management**: Organize books by categories
- ✅ **User Management**: Add, view, and deactivate users
- ✅ **Issue Tracking**: View all issued books and overdue items
- ✅ **Reservation Approval**: Approve or reject book reservations
- ✅ **Fine Management**: Configure fine settings and view collections
- ✅ **Reports**: Generate comprehensive library reports
- ✅ **Dashboard**: Real-time statistics and quick actions

### 👤 User Module
Regular users can:
- ✅ **Browse Books**: Search and filter available books
- ✅ **Issue Books**: Request to borrow available books
- ✅ **Return Books**: Return borrowed books with fine calculation
- ✅ **Reserve Books**: Book unavailable items for later
- ✅ **View History**: Check issued books, due dates, and fines
- ✅ **Dashboard**: Personal statistics and quick actions

### 💰 Fine Management System
- Configurable fine per day
- Configurable allowed days for book retention
- Automatic fine calculation on overdue books
- Fine tracking (paid/unpaid status)
- Total fine collection reports

### 📅 Advance Booking (Reservation)
- Users can reserve unavailable books
- Admin approval/rejection workflow
- Status tracking (Pending, Approved, Rejected, Fulfilled, Cancelled)
- Notification-ready system

### 📊 Reporting System
Reports available:
- Total books in library
- Available vs Issued books
- Overdue books with user details
- Active users count
- Fine collection summary
- Category-wise book distribution

---

## 🛠️ Technology Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Spring Boot 3.2.0 |
| **Security** | Spring Security 6 |
| **ORM** | Spring Data JPA (Hibernate) |
| **Database** | MySQL 8.0+ |
| **Frontend** | Thymeleaf Template Engine |
| **UI Framework** | Bootstrap 5.3 |
| **Icons** | Bootstrap Icons |
| **Build Tool** | Maven |
| **Java Version** | Java 17 |

---

## 📁 Project Structure

```
library-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/library/
│   │   │   ├── config/          # Security & configuration
│   │   │   ├── controller/      # Web controllers
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── service/         # Business logic
│   │   │   └── LibraryApplication.java
│   │   └── resources/
│   │       ├── templates/       # Thymeleaf HTML templates
│   │       │   ├── admin/       # Admin pages
│   │       │   └── user/        # User pages
│   │       ├── static/css/      # Custom stylesheets
│   │       └── application.properties
├── database/
│   └── schema.sql               # Database schema & sample data
├── pom.xml                      # Maven dependencies
└── README.md
```

---

## 🚀 Installation & Setup

### Prerequisites
- **Java 17** or higher
- **MySQL 8.0** or higher
- **Maven 3.6** or higher
- **IDE**: IntelliJ IDEA / Eclipse / VS Code

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd library-management-system
```

### Step 2: Setup MySQL Database
1. Login to MySQL:
```bash
mysql -u root -p
```

2. Create database and import schema:
```sql
source database/schema.sql
```

Alternatively, run the SQL file directly in MySQL Workbench or any MySQL client.

**Note**: The schema automatically creates:
- Database: `library_db`
- Sample categories (Fiction, Science, Technology, etc.)
- Sample books (10 books)
- Default admin user: `admin` / `admin123`
- Default test user: `john_doe` / `user123`

### Step 3: Configure Database Connection
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 4: Build the Project
```bash
mvn clean install
```

### Step 5: Run the Application
```bash
mvn spring-boot:run
```

Or run from IDE:
- Right-click `LibraryApplication.java` → Run

### Step 6: Access the Application
Open browser and navigate to:
```
http://localhost:8080
```

---

## 🔑 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| **Admin** | admin | admin123 |
| **User** | john_doe | user123 |

---

## 📖 Usage Guide

### For Administrators:

1. **Login** with admin credentials
2. **Dashboard** shows system statistics
3. **Manage Books**:
   - Add new books with ISBN, category, quantity
   - Edit existing book details
   - Delete books (if not issued)
   - Search books by title, author, or ISBN

4. **Manage Categories**:
   - Add new categories
   - View all categories

5. **Manage Users**:
   - Add new users
   - View all users
   - Deactivate users

6. **Track Issues**:
   - View all currently issued books
   - See overdue books
   - Track fines

7. **Handle Reservations**:
   - View pending reservations
   - Approve or reject requests
   - Add admin notes

8. **Configure Settings**:
   - Set allowed days (default: 14 days)
   - Set fine per day (default: ₹5)
   - Set max books per user (default: 3)

9. **Generate Reports**:
   - View comprehensive library statistics
   - Export data for analysis

### For Users:

1. **Register** a new account or login
2. **Browse Books**:
   - View all available books
   - Search by keywords
   - Filter by category

3. **Issue Books**:
   - Click "Issue" on available books
   - System checks user limits

4. **Return Books**:
   - Go to "My Books"
   - View due dates and fines
   - Return books (fine calculated automatically)

5. **Reserve Books**:
   - Reserve unavailable books
   - Wait for admin approval

6. **Track Status**:
   - View active issues
   - Check reservations
   - Monitor fines

---

## 🗄️ Database Schema

### Main Tables:
- **users**: User accounts (admin & regular users)
- **categories**: Book categories
- **books**: Book inventory
- **issued_books**: Issue/return transactions
- **reservations**: Book reservations
- **library_settings**: System configuration

### Key Relationships:
- Books → Category (Many-to-One)
- IssuedBooks → User (Many-to-One)
- IssuedBooks → Book (Many-to-One)
- Reservations → User (Many-to-One)
- Reservations → Book (Many-to-One)

---

## 🎨 Features Implemented

| Feature | Status |
|---------|--------|
| User Authentication | ✅ |
| Role-Based Access | ✅ |
| Book CRUD Operations | ✅ |
| Category Management | ✅ |
| User Management | ✅ |
| Book Issue System | ✅ |
| Book Return System | ✅ |
| Fine Calculation | ✅ |
| Advance Booking | ✅ |
| Reservation Approval | ✅ |
| Search & Filter | ✅ |
| Dashboard Statistics | ✅ |
| Report Generation | ✅ |
| Responsive Design | ✅ |
| Form Validation | ✅ |
| Error Handling | ✅ |

---

## 🔧 Configuration

### Fine Settings (Admin → Settings):
- **Allowed Days**: Number of days user can keep a book
- **Fine Per Day**: Amount charged per day for overdue books
- **Max Books Per User**: Maximum books a user can issue simultaneously

### Security:
- Passwords are encrypted using BCrypt
- CSRF protection enabled
- Role-based URL restrictions
- Session management

---

## 📝 API Endpoints Summary

### Public:
- `GET /` - Redirect to login
- `GET /login` - Login page
- `POST /login` - Authenticate user
- `GET /register` - Registration page
- `POST /register` - Create new user

### Admin:
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/books` - Manage books
- `GET /admin/categories` - Manage categories
- `GET /admin/users` - Manage users
- `GET /admin/issued-books` - View issues
- `GET /admin/reservations` - Manage reservations
- `GET /admin/settings` - Configure settings
- `GET /admin/reports` - View reports

### User:
- `GET /user/dashboard` - User dashboard
- `GET /user/books` - Browse books
- `POST /user/books/issue/{id}` - Issue book
- `POST /user/books/return/{id}` - Return book
- `POST /user/books/reserve/{id}` - Reserve book
- `GET /user/my-books` - View issued books
- `GET /user/my-reservations` - View reservations

---

## 🐛 Troubleshooting

### Common Issues:

1. **Database Connection Error**:
   - Check MySQL is running
   - Verify credentials in `application.properties`
   - Ensure database `library_db` exists

2. **Port Already in Use**:
   - Change port in `application.properties`:
   ```properties
   server.port=8081
   ```

3. **Login Not Working**:
   - Ensure user exists in database
   - Check password is correct
   - Verify user is active

4. **Books Not Showing**:
   - Run `schema.sql` to populate sample data
   - Check `books` table has data

---

## 🚀 Future Enhancements

Potential improvements:
- [ ] Email notifications for due dates and approvals
- [ ] PDF report generation
- [ ] Book cover image upload
- [ ] Advanced search with filters
- [ ] User profile management
- [ ] Book rating and reviews
- [ ] Reading history analytics
- [ ] Mobile app integration
- [ ] Barcode/QR code scanning
- [ ] Integration with external book APIs

---

## 👨‍💻 Developer Notes

### Running Tests:
```bash
mvn test
```

### Creating JAR:
```bash
mvn clean package
java -jar target/library-management-system-1.0.0.jar
```

### Hot Reload (Development):
Spring Boot DevTools is included - changes are auto-reloaded

---

## 📄 License

This project is created for educational and internship purposes.

---

## 🤝 Contributing

This is an internship project. For improvements:
1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

---

## 📧 Contact

For queries or support:
- **Email**: admin@library.com
- **GitHub**: [Your GitHub Profile]

---

## ⭐ Acknowledgments

- Spring Framework Team
- Bootstrap Team
- MySQL Community
- Open Source Community

---

**Built with ❤️ for learning and demonstration purposes**

---

## 🎯 Project Completion Checklist

- [x] Database design and schema
- [x] Entity classes with relationships
- [x] Repository layer
- [x] Service layer with business logic
- [x] Security configuration
- [x] Admin controllers
- [x] User controllers
- [x] Thymeleaf templates
- [x] Responsive UI design
- [x] Fine calculation logic
- [x] Reservation system
- [x] Dashboard with statistics
- [x] Search and filter functionality
- [x] Form validation
- [x] Error handling
- [x] Documentation

**Status**: ✅ **COMPLETE - Production Ready**
