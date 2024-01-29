package atm;

import java.util.ArrayList;
import java.util.Scanner;

class User {
	private int userId;
    private String password;
    private double balance;
    private ArrayList<String> transactions;

    public User(int userId, String password, double balance) {
        this.userId = userId;
        this.password = password;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public ArrayList<String> getTransactions() {
        return transactions;
    }

    public void addTransaction(String transaction) {
        transactions.add(transaction);
    }
}


class ATM {
	private ArrayList<User> users;

    public ATM(ArrayList<User> users) {
        this.users = users;
    }

    public User login(int enteredUserId, String enteredPassword) {
        for (User user : users) {
            if (user.getUserId() == enteredUserId && user.getPassword().equals(enteredPassword)) {
                return user;
            }
        }
        return null;
    }

    public void displayMenu() {
        System.out.println("1. Transactions History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    public void showBalance(User user) {
        System.out.println("Your balance: $" + user.getBalance());
    }
    
    public void displayTransactionsHistory(User user) {
        System.out.println("Transactions History:");
        ArrayList<String> transactions = user.getTransactions();
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
    }
    
    public void transfer(User currentUser, Scanner scanner) {
        System.out.print("Enter recipient's User ID: ");
        int recipientUserId;

        try {
            recipientUserId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid recipient User ID. Please enter a valid integer.");
            return;
        }

        // Find the recipient user
        User recipientUser = null;
        for (User user : users) {
            if (user.getUserId() == recipientUserId) {
                recipientUser = user;
                break;
            }
        }

        if (recipientUser == null || recipientUser == currentUser) {
            System.out.println("Invalid recipient User ID.");
            return;
        }

        System.out.print("Enter transfer amount: $");
        double transferAmount;

        try {
            transferAmount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid transfer amount. Please enter a valid number.");
            return;
        }

        if (transferAmount > 0 && transferAmount <= currentUser.getBalance()) {
            currentUser.setBalance(currentUser.getBalance() - transferAmount);
            recipientUser.setBalance(recipientUser.getBalance() + transferAmount);

            String transactionDetails = String.format("Transfer to %d: $%.2f", recipientUserId, transferAmount);
            currentUser.addTransaction(transactionDetails);

            String recipientTransactionDetails = String.format("Transfer from %d: $%.2f", currentUser.getUserId(), transferAmount);
            recipientUser.addTransaction(recipientTransactionDetails);

            System.out.println("Transfer successful. Remaining balance: $" + currentUser.getBalance());
        } else {
            System.out.println("Invalid transfer amount or insufficient funds.");
        }
    }

    // Additional method to handle withdrawal
    private void withdraw(User user, Scanner scanner) {
        System.out.print("Enter withdrawal amount: $");
        double withdrawAmount;

        try {
            withdrawAmount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid withdrawal amount. Please enter a valid number.");
            return;
        }

        if (withdrawAmount > 0 && withdrawAmount <= user.getBalance()) {
            user.setBalance(user.getBalance() - withdrawAmount);
            String transactionDetails = String.format("Withdrawal: $%.2f", withdrawAmount);
            user.addTransaction(transactionDetails);
            System.out.println("Withdrawal successful. Remaining balance: $" + user.getBalance());
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

    // Additional method to handle deposit
    private void deposit(User user, Scanner scanner) {
        System.out.print("Enter deposit amount: $");
        double depositAmount;

        try {
            depositAmount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid deposit amount. Please enter a valid number.");
            return;
        }

        if (depositAmount > 0) {
            user.setBalance(user.getBalance() + depositAmount);
            String transactionDetails = String.format("Deposit: $%.2f", depositAmount);
            user.addTransaction(transactionDetails);
            System.out.println("Deposit successful. Updated balance: $" + user.getBalance());
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }
    

    public void performTransaction(int choice, User user, Scanner scanner) {
        switch (choice) {
            case 1:
                System.out.println("Displaying Transactions History...");
                displayTransactionsHistory(user);
                break;
            case 2:
                withdraw(user, scanner);
                break;
            case 3:
                deposit(user, scanner);
                break;
            case 4:
                transfer(user, scanner);
                break;
            case 5:
                System.out.println("Thank you for using the ATM. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

}

public class Main {
    	public static void main(String[] args) {
            ArrayList<User> users = new ArrayList<>();
            users.add(new User(12345, "pass123", 1000.0));  // Sample user with ID 12345, password "pass123", and initial balance $1000
            // Add more users as needed

            ATM atm = new ATM(users);
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter User ID: ");
            int enteredUserId = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            System.out.print("Enter Password: ");
            String enteredPassword = scanner.nextLine();  // Read entire line to handle spaces

            User currentUser = atm.login(enteredUserId, enteredPassword);

        if (currentUser != null) {
            System.out.println("Login successful!");
            while (true) {
                atm.displayMenu();
                System.out.print("Enter your choice (1-5): ");
                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please enter a valid number.");
                    continue;
                }
                atm.performTransaction(choice, currentUser, scanner);
                atm.showBalance(currentUser);
            }
        } else {
            System.out.println("Invalid User ID or Password. Exiting...");
        }
    }
}
