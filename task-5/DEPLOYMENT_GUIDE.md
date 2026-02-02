# 🚀 Modern Digital Library - Deployment Guide

## 📋 Quick Start Guide for Non-Technical Users

This guide will help you set up and run the Modern Digital Library on your computer.

---

## 🎯 What You Need

Before starting, download and install these programs:

### 1. Java Development Kit (JDK 17)
- **Download**: [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)
- Select: Java 17 (LTS)
- Install and verify:
  ```bash
  java -version
  # Should show: java version "17.x.x"
  ```

### 2. MySQL Database
- **Download**: [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)
- Install MySQL Community Server 8.0+
- Remember your root password!

### 3. Maven (Build Tool)
- **Download**: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
- Or if using IntelliJ IDEA, it comes built-in

### 4. IDE (Recommended)
- **IntelliJ IDEA Community Edition** (Free): [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
- Or Eclipse: [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/)
- Or VS Code with Java Extension Pack

---

## ⚡ Quick Setup (5 Steps)

### Step 1: Setup MySQL Database

1. Open **MySQL Workbench** or **MySQL Command Line**

2. Login with your root password:
```sql
mysql -u root -p
```

3. Copy the entire content of `database/schema.sql` file

4. Paste and execute in MySQL

5. You should see:
```
Database schema created successfully!
Default admin credentials - Username: admin, Password: admin123
```

### Step 2: Configure Database Connection

1. Open the project folder

2. Go to: `src/main/resources/application.properties`

3. Update these lines with YOUR MySQL password:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
```

### Step 3: Open in IDE

**For IntelliJ IDEA**:
1. Open IntelliJ IDEA
2. Click "Open"
3. Select the `library-management-system` folder
4. Wait for Maven to download dependencies (this may take a few minutes)

**For Eclipse**:
1. File → Import → Maven → Existing Maven Projects
2. Browse to project folder
3. Click Finish

### Step 4: Run the Application

**Method 1: Using IDE**
1. Find `LibraryApplication.java` in `src/main/java/com/library/`
2. Right-click → Run 'LibraryApplication'

**Method 2: Using Command Line**
```bash
cd library-management-system
mvn spring-boot:run
```

**Method 3: Using Maven Wrapper (if available)**
```bash
./mvnw spring-boot:run        # Mac/Linux
mvnw.cmd spring-boot:run      # Windows
```

### Step 5: Access the Application

1. Wait for the application to start (you'll see "Started LibraryApplication")

2. Open your browser

3. Go to: **http://localhost:8080**

4. Login with:
   - Admin: `admin` / `admin123`
   - User: `john_doe` / `user123`

🎉 **You're done!**

---

## 🔧 Common Problems & Solutions

### Problem 1: "Port 8080 already in use"
**Solution**: Change the port
1. Edit `application.properties`
2. Add: `server.port=8081`
3. Access at: `http://localhost:8081`

### Problem 2: "Cannot connect to database"
**Solutions**:
- ✅ Check MySQL is running (Windows: Services, Mac: System Preferences)
- ✅ Verify password in `application.properties`
- ✅ Ensure database `library_db` exists
- ✅ Check MySQL is on port 3306

### Problem 3: "Login not working"
**Solutions**:
- ✅ Make sure you ran the `schema.sql` file
- ✅ Check the users table has data: `SELECT * FROM users;`
- ✅ Use correct credentials: `admin` / `admin123`

### Problem 4: Maven build fails
**Solutions**:
- ✅ Check internet connection (Maven downloads dependencies)
- ✅ Delete `.m2/repository` folder and try again
- ✅ Run: `mvn clean install -U`

### Problem 5: "ClassNotFoundException" or compile errors
**Solutions**:
- ✅ Reimport Maven project
- ✅ Run: `mvn clean compile`
- ✅ Check Java version: `java -version` (must be 17+)

---

## 🖥️ Deployment to Production Server

### Option 1: Traditional Server Deployment

1. **Build JAR file**:
```bash
mvn clean package
```

2. **Copy JAR** from `target/library-management-system-1.0.0.jar` to your server

3. **Run on server**:
```bash
java -jar library-management-system-1.0.0.jar
```

4. **Configure external properties** (create `application.properties` in same folder):
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://YOUR_DB_HOST:3306/library_db
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
```

5. **Run as background service** (Linux):
```bash
nohup java -jar library-management-system-1.0.0.jar &
```

### Option 2: Cloud Deployment (AWS/Azure/GCP)

**For AWS Elastic Beanstalk**:
1. Package: `mvn clean package`
2. Upload JAR to Elastic Beanstalk
3. Configure RDS MySQL database
4. Set environment variables

**For Heroku**:
1. Create `Procfile`:
```
web: java -jar target/library-management-system-1.0.0.jar
```
2. Use ClearDB MySQL addon
3. Deploy via Git

### Option 3: Docker Deployment

Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/library-management-system-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t library-system .
docker run -p 8080:8080 library-system
```

---

## 📊 Testing the Application

### Test as Admin:
1. Login with `admin` / `admin123`
2. Add a new book
3. Add a new user
4. View reports
5. Manage settings

### Test as User:
1. Login with `john_doe` / `user123`
2. Browse books
3. Issue a book
4. Return the book
5. Reserve a book

---

## 🔐 Security Best Practices

Before production deployment:

1. **Change default passwords**:
```sql
UPDATE users SET password = '$2a$10$NEW_BCRYPT_HASH' WHERE username = 'admin';
```

2. **Enable HTTPS**:
- Get SSL certificate
- Configure in `application.properties`:
```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=YOUR_PASSWORD
```

3. **Secure database**:
- Create dedicated MySQL user (not root)
- Grant only necessary permissions
- Use strong passwords

4. **Configure firewall**:
- Allow only necessary ports
- Block direct database access

---

## 📱 Accessing from Other Devices

1. **Find your IP address**:
   - Windows: `ipconfig`
   - Mac/Linux: `ifconfig`

2. **Access from same network**:
   - Other devices: `http://YOUR_IP:8080`
   - Example: `http://192.168.1.100:8080`

3. **Allow firewall**:
   - Windows: Add inbound rule for port 8080
   - Mac: System Preferences → Security → Firewall

---

## 🆘 Getting Help

### Where to look:

1. **Application Logs**:
```bash
# In terminal where app is running, check output
# Or check logs/ folder
```

2. **Database Issues**:
```sql
# Check data
SELECT * FROM users;
SELECT * FROM books;
SELECT * FROM issued_books;
```

3. **Check Configuration**:
```bash
# Verify application.properties is correct
# Check MySQL is running
```

---

## ✅ Pre-Launch Checklist

Before showing to your supervisor/client:

- [ ] Database is populated with sample data
- [ ] All features working (add book, issue, return, etc.)
- [ ] Both admin and user logins work
- [ ] No errors in console/logs
- [ ] UI looks good on different screen sizes
- [ ] Search functionality works
- [ ] Reports generate correctly
- [ ] Fine calculation is accurate
- [ ] Reservation system works

---

## 🎓 For Your Internship Presentation

**Key points to highlight**:
1. Full-stack development (Backend + Frontend + Database)
2. Enterprise design patterns (MVC, Service Layer, Repository)
3. Security implementation (Spring Security, BCrypt)
4. Role-based access control
5. Complex business logic (fine calculation, availability tracking)
6. Professional UI/UX with Bootstrap
7. Database normalization and relationships
8. RESTful architecture
9. Modern Java/Spring Boot techniques

**Demo Flow**:
1. Show architecture diagram
2. Explain database schema
3. Live demo as Admin (add book, manage users)
4. Live demo as User (issue, return book)
5. Show reports and statistics
6. Explain key code sections
7. Discuss challenges and solutions

---

## 📖 Project Highlights

**Lines of Code**: ~3000+ lines
**Technologies Used**: 8+ (Spring Boot, MySQL, Security, JPA, Thymeleaf, Bootstrap, Maven, Java)
**Features Implemented**: 15+ major features
**Database Tables**: 6 tables with relationships
**User Roles**: 2 (Admin, User)
**CRUD Operations**: Complete implementation

---

## 💡 Tips for Success

1. **Understand the code**: Don't just copy-paste, understand each component
2. **Test thoroughly**: Try all features before demo
3. **Document well**: Keep this README handy
4. **Be prepared**: Know how to fix common issues
5. **Explain clearly**: Practice explaining the project

---

## 📚 Additional Resources

**Learn More About**:
- Spring Boot: [https://spring.io/guides](https://spring.io/guides)
- Spring Security: [https://spring.io/guides/gs/securing-web/](https://spring.io/guides/gs/securing-web/)
- Thymeleaf: [https://www.thymeleaf.org/documentation.html](https://www.thymeleaf.org/documentation.html)
- MySQL: [https://dev.mysql.com/doc/](https://dev.mysql.com/doc/)
- Bootstrap: [https://getbootstrap.com/docs/](https://getbootstrap.com/docs/)

---

**Good luck with your internship! 🚀**

If you encounter any issues not covered here, check the logs and error messages carefully - they usually point to the problem!
