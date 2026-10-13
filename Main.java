import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static List<User> users = new ArrayList<>();
    private static List<Account> accounts = new ArrayList<>();
    private static List<Transaction> transactions = new ArrayList<>();


    // ==========================================
    // MAIN METHOD
    // ==========================================

    public static void main(String[] args) {

        // Load saved encrypted data when application starts
        users = FileManager.loadUsers();
        accounts = FileManager.loadAccounts();
        transactions = FileManager.loadTransactions();

        boolean running = true;

        while (running) {

            System.out.println("\n=================================");
            System.out.println("       SECURE BANKING SYSTEM");
            System.out.println("=================================");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("\nChoose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    createAccount();
                    break;

                case "2":
                    login();
                    break;

                case "3":
                    running = false;

                    System.out.println(
                            "\nThank you for using Secure Banking System."
                    );

                    break;

                default:
                    System.out.println(
                            "\nInvalid option. Please try again."
                    );
            }
        }

        scanner.close();
    }


    // ==========================================
    // CREATE ACCOUNT
    // ==========================================

    private static void createAccount() {

        System.out.println(
                "\n---------- CREATE ACCOUNT ----------"
        );

        System.out.print("Enter username: ");

        String username =
                scanner.nextLine().trim();


        // Allowlist username validation
        if (username.length() < 3 ||
                username.length() > 30) {

            System.out.println(
                    "Username must be between 3 and 30 characters."
            );

            return;
        }


        if (!username.matches("[A-Za-z0-9_]+")) {

            System.out.println(
                    "Invalid username. Only letters, numbers "
                            + "and underscore (_) are allowed."
            );

            return;
        }


        // Check whether username already exists
        for (User user : users) {

            if (user.getUsername()
                    .equalsIgnoreCase(username)) {

                System.out.println(
                        "Username already exists. Try another one."
                );

                return;
            }
        }


        // Read password securely
        String password = readPassword();


        if (password.length() < 8) {

            System.out.println(
                    "Password must be at least 8 characters."
            );

            return;
        }


        // Hash password before storing it
        String passwordHash =
                PasswordHasher.hashPassword(password);


        User user =
                new User(
                        username,
                        passwordHash
                );


        // Generate account number
        String accountNumber =
                String.format(
                        "%06d",
                        100000 + accounts.size() + 1
                );


        Account account =
                new Account(
                        accountNumber,
                        username,
                        0.0
                );


        // Save encrypted user data
        boolean userSaved =
                FileManager.saveUser(user);


        // Save encrypted account data
        boolean accountSaved =
                FileManager.saveAccount(account);


        if (userSaved && accountSaved) {

            // Only add to memory after successful saving
            users.add(user);
            accounts.add(account);

            System.out.println(
                    "\nAccount created successfully!"
            );

            System.out.println(
                    "Your account number is: "
                            + accountNumber
            );

        } else {

            System.out.println(
                    "\nAccount creation failed."
            );

            System.out.println(
                    "Your information was not saved."
            );
        }
    }


    // ==========================================
    // LOGIN
    // ==========================================
private static void login() {

    System.out.println(
            "\n---------- LOGIN ----------"
    );

    // Accept all login inputs first
    System.out.print("Enter username: ");

    String username =
            scanner.nextLine().trim();


    System.out.print("Enter account number: ");

    String accountNumber =
            scanner.nextLine().trim();


    String password =
            readPassword();


    User loggedInUser = null;
    Account userAccount = null;


    // Find the user
    for (User user : users) {

        if (user.getUsername()
                .equalsIgnoreCase(username)) {

            loggedInUser = user;

            break;
        }
    }


    // Find the account belonging to the username
    if (loggedInUser != null) {

        for (Account account : accounts) {

            if (account.getUsername()
                    .equalsIgnoreCase(username)
                    &&
                    account.getAccountNumber()
                            .equals(accountNumber)) {

                userAccount = account;

                break;
            }
        }
    }


    // Check all credentials AFTER all inputs have been entered
    boolean validUsername =
            loggedInUser != null;

    boolean validAccount =
            userAccount != null;

    boolean validPassword =
            validUsername &&
            PasswordHasher.verifyPassword(
                    password,
                    loggedInUser.getPasswordHash()
            );


    // If any credential is invalid
    if (!validUsername ||
            !validAccount ||
            !validPassword) {

        System.out.println(
                "\nLogin failed. Invalid credentials."
        );

        return;
    }


    // Check if account is already locked
    if (loggedInUser.isLocked()) {

        System.out.println(
                "\nAccount is locked due to multiple failed "
                        + "login attempts. Contact Administrator."
        );

        return;
    }


    // Successful login
    loggedInUser.resetFailedAttempts();


    boolean userUpdated =
            FileManager.updateUser(
                    loggedInUser
            );


    if (!userUpdated) {

        System.out.println(
                "Warning: Login status could not be saved."
        );
    }


    System.out.println(
            "\nLogin successful!"
    );


    accountMenu(loggedInUser);
}


    // ==========================================
    // ACCOUNT MENU
    // ==========================================

    private static void accountMenu(User user) {

        Account account =
                findAccount(
                        user.getUsername()
                );


        if (account == null) {

            System.out.println(
                    "Account information could not be found."
            );

            return;
        }


        boolean loggedIn = true;


        while (loggedIn) {

            System.out.println(
                    "\n================================="
            );

            System.out.println(
                    "          ACCOUNT MENU"
            );

            System.out.println(
                    "================================="
            );

            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println( "3. Withdraw");
            System.out.println("4. Transaction History");
            System.out.println("5. Logout");
            

            System.out.print("\nChoose an option: ");


            String choice = scanner.nextLine();


            switch (choice) {

                case "1":

                    viewBalance(account);

                    break;


                case "2":

                    deposit(account);

                    break;


                case "3":

                    withdraw(account);

                    break;


                case "4":

                    showTransactionHistory(
                            account
                    );

                    break;


                case "5":

                    loggedIn = false;

                    System.out.println(
                            "\nLogged out successfully."
                    );

                    break;


                default:

                    System.out.println(
                            "\nInvalid option."
                    );
            }
        }
    }


    // ==========================================
    // VIEW BALANCE
    // ==========================================

    private static void viewBalance(
            Account account) {

        System.out.println(
                "\n---------- ACCOUNT DETAILS ----------"
        );

        System.out.println(
                "Account Number: "
                        + account.getAccountNumber()
        );

        System.out.println(
                "Balance: $"
                        + String.format(
                                "%.2f",
                                account.getBalance()
                        )
        );
    }


    // ==========================================
    // DEPOSIT
    // ==========================================

    private static void deposit(
            Account account) {

        System.out.print(
                "\nEnter deposit amount: "
        );


        String input =
                scanner.nextLine();


        try {

            double amount =
                    Double.parseDouble(input);


            if (amount <= 0) {

                System.out.println(
                        "Amount must be greater than zero."
                );

                return;
            }


            // Store old balance in case saving fails
            double oldBalance =
                    account.getBalance();


            account.deposit(amount);


            String transactionId =
                    generateTransactionId();


            Transaction transaction =
                    new Transaction(
                            transactionId,
                            account.getAccountNumber(),
                            "DEPOSIT",
                            amount,
                            LocalDateTime.now()
                    );


            // Save updated account first
            boolean accountSaved =
                    updateAccountFile(account);


            if (!accountSaved) {

                // Restore original balance
                restoreAccountBalance(
                        account,
                        oldBalance
                );

                System.out.println(
                        "Deposit failed. Account data "
                                + "could not be saved."
                );

                return;
            }


            // Save transaction
            boolean transactionSaved =
                    FileManager.saveTransaction(
                            transaction
                    );


            if (!transactionSaved) {

                // Roll back account balance
                restoreAccountBalance(
                        account,
                        oldBalance
                );

                System.out.println(
                        "Deposit failed. Transaction "
                                + "could not be saved."
                );

                return;
            }


            transactions.add(transaction);


            System.out.println(
                    "Deposit successful. New balance: $"
                            + String.format(
                                    "%.2f",
                                    account.getBalance()
                            )
            );


        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid amount."
            );
        }
    }


    // ==========================================
    // WITHDRAW
    // ==========================================

    private static void withdraw(
            Account account) {

        System.out.print(
                "\nEnter withdrawal amount: "
        );


        String input =
                scanner.nextLine();


        try {

            double amount =
                    Double.parseDouble(input);


            if (amount <= 0) {

                System.out.println(
                        "Amount must be greater than zero."
                );

                return;
            }


            if (amount >
                    account.getBalance()) {

                System.out.println(
                        "Insufficient funds."
                );

                return;
            }


            double oldBalance =
                    account.getBalance();


            boolean successful =
                    account.withdraw(amount);


            if (!successful) {

                System.out.println(
                        "Withdrawal failed."
                );

                return;
            }


            String transactionId =
                    generateTransactionId();


            Transaction transaction =
                    new Transaction(
                            transactionId,
                            account.getAccountNumber(),
                            "WITHDRAW",
                            amount,
                            LocalDateTime.now()
                    );


            // Save updated account
            boolean accountSaved =
                    updateAccountFile(account);


            if (!accountSaved) {

                // Restore balance if saving fails
                restoreAccountBalance(
                        account,
                        oldBalance
                );

                System.out.println(
                        "Withdrawal failed. Account data "
                                + "could not be saved."
                );

                return;
            }


            // Save transaction
            boolean transactionSaved =
                    FileManager.saveTransaction(
                            transaction
                    );


            if (!transactionSaved) {

                // Restore balance if transaction fails
                restoreAccountBalance(
                        account,
                        oldBalance
                );

                System.out.println(
                        "Withdrawal failed. Transaction "
                                + "could not be saved."
                );

                return;
            }


            transactions.add(transaction);


            System.out.println(
                    "Withdrawal successful. New balance: $"
                            + String.format(
                                    "%.2f",
                                    account.getBalance()
                            )
            );


        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid amount."
            );
        }
    }


    // ==========================================
    // TRANSACTION HISTORY
    // ==========================================

    private static void showTransactionHistory(
            Account account) {

        System.out.println(
                "\n---------- TRANSACTION HISTORY ----------"
        );


        boolean found = false;


        for (Transaction transaction :
                transactions) {

            if (transaction
                    .getAccountNumber()
                    .equals(
                            account.getAccountNumber()
                    )) {


                System.out.println(
                        transaction
                                .getTransactionId()
                                + " | "
                                + transaction
                                .getType()
                                + " | $"
                                + String.format(
                                        "%.2f",
                                        transaction
                                                .getAmount()
                                )
                                + " | "
                                + transaction
                                .getDateTime()
                );


                found = true;
            }
        }


        if (!found) {

            System.out.println(
                    "No transactions found."
            );
        }
    }


    // ==========================================
    // FIND ACCOUNT
    // ==========================================

    private static Account findAccount(
            String username) {

        for (Account account :
                accounts) {

            if (account
                    .getUsername()
                    .equalsIgnoreCase(username)) {

                return account;
            }
        }


        return null;
    }


    // ==========================================
    // UPDATE ACCOUNT FILE
    // ==========================================

    private static boolean updateAccountFile(
            Account updatedAccount) {

        try {

            StringBuilder data =
                    new StringBuilder();


            for (Account account :
                    accounts) {

                data.append(
                        account.getAccountNumber()
                ).append("|");

                data.append(
                        account.getUsername()
                ).append("|");

                data.append(
                        account.getBalance()
                ).append("\n");
            }


            // Encrypt the entire accounts data
            String encryptedData =
                    EncryptionUtil.encrypt(
                            data.toString()
                    );


            try (FileWriter writer =
                         new FileWriter(
                                 "accounts.txt"
                         )) {

                writer.write(encryptedData);
            }


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error updating encrypted account data."
            );

            System.out.println(
                    "Reason: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ==========================================
    // RESTORE ACCOUNT BALANCE
    // ==========================================

    private static void restoreAccountBalance(
            Account account,
            double oldBalance) {

        try {

            StringBuilder data =
                    new StringBuilder();


            for (Account currentAccount :
                    accounts) {

                if (currentAccount
                        .getAccountNumber()
                        .equals(
                                account.getAccountNumber()
                        )) {

                    data.append(
                            account.getAccountNumber()
                    ).append("|");

                    data.append(
                            account.getUsername()
                    ).append("|");

                    data.append(
                            oldBalance
                    ).append("\n");

                } else {

                    data.append(
                            currentAccount
                                    .getAccountNumber()
                    ).append("|");

                    data.append(
                            currentAccount
                                    .getUsername()
                    ).append("|");

                    data.append(
                            currentAccount
                                    .getBalance()
                    ).append("\n");
                }
            }


            String encryptedData =
                    EncryptionUtil.encrypt(
                            data.toString()
                    );


            try (FileWriter writer =
                         new FileWriter(
                                 "accounts.txt"
                         )) {

                writer.write(encryptedData);
            }


        } catch (Exception e) {

            System.out.println(
                    "Warning: Could not restore account balance."
            );
        }
    }


    // ==========================================
    // READ PASSWORD SECURELY
    // ==========================================

    private static String readPassword() {

        Console console =
                System.console();


        if (console != null) {

            char[] password =
                    console.readPassword(
                            "Enter password: "
                    );

            return new String(password);

        } else {

            // Fallback for IDEs such as VS Code
            System.out.print(
                    "Enter password: "
            );

            return scanner.nextLine();
        }
    }


    // ==========================================
    // GENERATE TRANSACTION ID
    // ==========================================

    private static String generateTransactionId() {

        return "TXN"
                + String.format(
                        "%04d",
                        transactions.size() + 1
                );
    }
}