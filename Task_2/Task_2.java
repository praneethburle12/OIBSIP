import java.util.Scanner;
import java.util.Random;

public class Task_2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalRounds = 0;
        int totalWins = 0;
        int totalLosses = 0;
        int totalScore = 0;

        System.out.println("Welcome to the Number Guessing Game!");

        // Loop to handle multiple rounds [2, 3]
        boolean playAgain = true;
        while (playAgain) {
            totalRounds++;
            int roundScore = playRound(sc);

            if (roundScore > 0) {
                totalWins++;
            } else {
                totalLosses++;
            }
            totalScore += roundScore;

            System.out.print("\nDo you want to play again? (yes/no): ");
            String response = sc.next().trim().toLowerCase();
            if (response.equals("no")) {
                playAgain = false;
            }
        }

        showSummary(totalRounds, totalWins, totalLosses, totalScore);
        sc.close();
    }

    static int generateNumber() {
        Random random = new Random();
        return random.nextInt(100) + 1;
    }

    static int playRound(Scanner sc) {
        int targetNumber = generateNumber();
        int maxAttempts = 7;
        int attemptsUsed = 0;
        boolean hasWon = false;

        System.out.println("\n--- New Round ---");
        System.out.println("I have chosen a number between 1 and 100. Can you guess it?");

        while (attemptsUsed < maxAttempts) {
            System.out.print("Attempt " + (attemptsUsed + 1) + "/" + maxAttempts + " - Enter your guess: ");

            int userGuess;
            try {
                if (!sc.hasNextInt()) {
                    System.out.println("Invalid input! Please enter an integer.");
                    sc.next();
                    continue;
                }
                userGuess = sc.nextInt();

                if (userGuess < 1 || userGuess > 100) {
                    System.out.println("Please guess a number between 1 and 100.");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
                sc.next();
                continue;
            }

            attemptsUsed++;
            if (userGuess < targetNumber) {
                System.out.println("Too low! Try again.");
            } else if (userGuess > targetNumber) {
                System.out.println("Too high! Try again.");
            } else {
                System.out.println("Correct! You guessed the number!");
                hasWon = true;
                break;
            }
        }

        if (!hasWon) {
            System.out.println("You lost this round. The number was: " + targetNumber);
        }

        int score = calculateScore(attemptsUsed, hasWon);
        System.out.println("Round Score: " + score);
        return score;
    }

    static int calculateScore(int attemptsUsed, boolean won) {
        if (!won)
            return 0;

        if (attemptsUsed == 1)
            return 100;
        if (attemptsUsed <= 3)
            return 70;
        if (attemptsUsed <= 5)
            return 50;
        if (attemptsUsed <= 7)
            return 30;

        return 0;
    }

    static void showSummary(int rounds, int wins, int losses, int totalScore) {
        double average = (rounds == 0) ? 0 : (double) totalScore / rounds;

        System.out.println("\n========== GAME SUMMARY ==========");
        System.out.println("Total Rounds Played: " + rounds);
        System.out.println("Rounds Won: " + wins);
        System.out.println("Rounds Lost: " + losses);
        System.out.println("Total Score: " + totalScore);
        System.out.printf("Average Score per Round: %.2f\n", average);
        System.out.println("==================================");
    }
}