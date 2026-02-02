package onlineexamination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class QuizSession {
    private User currentUser;
    private Map<String, User> usersPreview = new HashMap<>(); // Mock DB
    private List<Question> questions = new ArrayList<>();
    private Scanner scanner;

    public QuizSession() {
        // Initialize mock users
        usersPreview.put("admin", new User("admin", "1234", "Administrator", "admin@test.com"));
        usersPreview.put("user", new User("user", "pass", "Standard User", "user@test.com"));

        // Initialize questions
        initializeQuestions();
        scanner = new Scanner(System.in);
    }

    private void initializeQuestions() {
        questions.add(new Question("What is the capital of France?",
                List.of("London", "Berlin", "Paris", "Madrid"), 3));
        questions.add(new Question("Which data type is used to create a variable that should store text?",
                List.of("String", "int", "float", "boolean"), 1));
        questions.add(new Question("How do you create a method in Java?",
                List.of("methodName[]", "methodName()", "(methodName)", "{}"), 2));
        questions.add(new Question("What is the size of int in Java?",
                List.of("8 bit", "16 bit", "32 bit", "64 bit"), 3));
        questions.add(new Question("Which keyword is used to create a class in Java?",
                List.of("class", "Class", "new", "object"), 1));
    }

    public void start() {
        System.out.println("Welcome to the Online Examination System");
        while (true) {
            if (currentUser == null) {
                login();
            } else {
                showMainMenu();
            }
        }
    }

    private void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = usersPreview.get(username);
        if (user != null && user.validatePassword(password)) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + user.getUsername());
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Update Profile and Password");
        System.out.println("2. Start Exam");
        System.out.println("3. Logout");
        System.out.print("Select an option: ");

        String input = scanner.nextLine();
        switch (input) {
            case "1":
                updateProfile();
                break;
            case "2":
                startExam();
                break;
            case "3":
                logout();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void updateProfile() {
        System.out.println("\n--- UPDATE PROFILE ---");
        System.out.println("Current Profile: " + currentUser);
        System.out.println("Leave blank to keep current value.");

        System.out.print("New Full Name: ");
        String name = scanner.nextLine();
        System.out.print("New Email: ");
        String email = scanner.nextLine();

        currentUser.updateProfile(name, email);

        System.out.print("New Password (or blank to keep): ");
        String pass = scanner.nextLine();
        if (!pass.isEmpty()) {
            currentUser.updatePassword(pass);
            System.out.println("Password updated.");
        }
        System.out.println("Profile updated successfully!");
    }

    private void logout() {
        System.out.println("Logging out...");
        currentUser = null;
    }

    private void startExam() {
        System.out.println("\n--- EXAM STARTED ---");
        System.out.println("You have 1 minute to complete the exam.");

        int score = 0;
        int timeLimitSeconds = 60;
        long endTime = System.currentTimeMillis() + (timeLimitSeconds * 1000);

        for (int i = 0; i < questions.size(); i++) {
            if (System.currentTimeMillis() > endTime) {
                System.out.println("\nTime is up! Auto-submitting...");
                break; // Exit loop
            }

            Question q = questions.get(i);
            long remainingSeconds = (endTime - System.currentTimeMillis()) / 1000;
            System.out.println("\nQuestion " + (i + 1) + " (" + remainingSeconds + "s remaining):");
            System.out.println(q.getQuestionText());
            List<String> opts = q.getOptions();
            for (int j = 0; j < opts.size(); j++) {
                System.out.println((j + 1) + ". " + opts.get(j));
            }

            System.out.print("Your answer (1-4): ");
            // Use a non-blocking check or just check time after input.
            // For console simplicity, we check time before displaying and after input.
            // If they wait too long on the prompt, we'll catch it after they eventually
            // press enter (or if we had a separate thread interrupting, but standard
            // Scanner blocks).
            // A simple "Time check" approach is standard for simple console apps.

            String answerStr = scanner.nextLine();

            if (System.currentTimeMillis() > endTime) {
                System.out.println("\nTime is up! Your last answer was not recorded.");
                break;
            }

            try {
                int answer = Integer.parseInt(answerStr);
                if (answer < 1 || answer > 4) {
                    System.out.println("Invalid option. access denied code 0.");
                } else {
                    if (q.isCorrect(answer)) {
                        score++;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. access denied code 0.");
            }
        }

        System.out.println("\n--- EXAM FINISHED ---");
        System.out.println("Your Score: " + score + "/" + questions.size());
    }
}
