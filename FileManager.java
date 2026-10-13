import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String USERS_FILE = "users.txt";
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";


    // =========================
    // USER METHODS
    // =========================

    public static boolean saveUser(User user) {

        List<User> users = loadUsers();

        users.add(user);

        return saveAllUsers(users);
    }


    public static List<User> loadUsers() {

        List<User> users = new ArrayList<>();

        String data = readEncryptedFile(USERS_FILE);

        if (data.isEmpty()) {
            return users;
        }

        String[] lines = data.split("\\R");

        for (String line : lines) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|");

            if (parts.length >= 4) {

                try {

                    User user = new User(
                            parts[0],
                            parts[1],
                            Integer.parseInt(parts[2]),
                            Boolean.parseBoolean(parts[3])
                    );

                    users.add(user);

                } catch (Exception e) {

                    System.out.println(
                            "Invalid user data found."
                    );
                }
            }
        }

        return users;
    }


    // =========================
    // UPDATE USER
    // =========================

    public static boolean updateUser(User updatedUser) {

        List<User> users = loadUsers();

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i)
                    .getUsername()
                    .equals(updatedUser.getUsername())) {

                users.set(i, updatedUser);
                break;
            }
        }

        return saveAllUsers(users);
    }


    private static boolean saveAllUsers(
            List<User> users) {

        StringBuilder data =
                new StringBuilder();

        for (User user : users) {

            data.append(
                    user.getUsername()
            ).append("|");

            data.append(
                    user.getPasswordHash()
            ).append("|");

            data.append(
                    user.getFailedLoginAttempts()
            ).append("|");

            data.append(
                    user.isLocked()
            ).append("\n");
        }

        return writeEncryptedFile(
                USERS_FILE,
                data.toString()
        );
    }


    // =========================
    // ACCOUNT METHODS
    // =========================

    public static boolean saveAccount(
            Account account) {

        List<Account> accounts =
                loadAccounts();

        accounts.add(account);

        return saveAllAccounts(accounts);
    }


    public static List<Account> loadAccounts() {

        List<Account> accounts =
                new ArrayList<>();

        String data =
                readEncryptedFile(ACCOUNTS_FILE);

        if (data.isEmpty()) {
            return accounts;
        }

        String[] lines =
                data.split("\\R");

        for (String line : lines) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts =
                    line.split("\\|");

            if (parts.length >= 3) {

                try {

                    Account account =
                            new Account(
                                    parts[0],
                                    parts[1],
                                    Double.parseDouble(parts[2])
                            );

                    accounts.add(account);

                } catch (Exception e) {

                    System.out.println(
                            "Invalid account data found."
                    );
                }
            }
        }

        return accounts;
    }


    // =========================
    // SAVE ALL ACCOUNTS
    // =========================

    private static boolean saveAllAccounts(
            List<Account> accounts) {

        StringBuilder data =
                new StringBuilder();

        for (Account account : accounts) {

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

        return writeEncryptedFile(
                ACCOUNTS_FILE,
                data.toString()
        );
    }


    // =========================
    // TRANSACTION METHODS
    // =========================

    public static boolean saveTransaction(
            Transaction transaction) {

        List<Transaction> transactions =
                loadTransactions();

        transactions.add(transaction);

        return saveAllTransactions(
                transactions
        );
    }


    public static List<Transaction> loadTransactions() {

        List<Transaction> transactions =
                new ArrayList<>();

        String data =
                readEncryptedFile(
                        TRANSACTIONS_FILE
                );

        if (data.isEmpty()) {
            return transactions;
        }

        String[] lines =
                data.split("\\R");

        for (String line : lines) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts =
                    line.split("\\|");

            if (parts.length >= 5) {

                try {

                    Transaction transaction =
                            new Transaction(
                                    parts[0],
                                    parts[1],
                                    parts[2],
                                    Double.parseDouble(parts[3]),
                                    LocalDateTime.parse(parts[4])
                            );

                    transactions.add(transaction);

                } catch (Exception e) {

                    System.out.println(
                            "Invalid transaction data found."
                    );
                }
            }
        }

        return transactions;
    }


    // =========================
    // SAVE ALL TRANSACTIONS
    // =========================

    private static boolean saveAllTransactions(
            List<Transaction> transactions) {

        StringBuilder data =
                new StringBuilder();

        for (Transaction transaction :
                transactions) {

            data.append(
                    transaction.getTransactionId()
            ).append("|");

            data.append(
                    transaction.getAccountNumber()
            ).append("|");

            data.append(
                    transaction.getType()
            ).append("|");

            data.append(
                    transaction.getAmount()
            ).append("|");

            data.append(
                    transaction.getDateTime()
            ).append("\n");
        }

        return writeEncryptedFile(
                TRANSACTIONS_FILE,
                data.toString()
        );
    }


    // =========================
    // ENCRYPTED FILE READING
    // =========================

    private static String readEncryptedFile(
            String fileName) {

        File file =
                new File(fileName);

        if (!file.exists()) {
            return "";
        }

        try {

            StringBuilder encryptedData =
                    new StringBuilder();

            try (BufferedReader reader =
                         new BufferedReader(
                                 new FileReader(file))) {

                String line;

                while ((line =
                        reader.readLine()) != null) {

                    encryptedData.append(line);
                }
            }

            if (encryptedData.length() == 0) {
                return "";
            }

            return EncryptionUtil.decrypt(
                    encryptedData.toString()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error decrypting " + fileName
            );

            return "";
        }
    }


    // =========================
    // ENCRYPTED FILE WRITING
    // =========================

    private static boolean writeEncryptedFile(
            String fileName,
            String data) {

        try {

            String encryptedData =
                    EncryptionUtil.encrypt(data);

            try (FileWriter writer =
                         new FileWriter(fileName)) {

                writer.write(encryptedData);
            }

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error encrypting " + fileName
            );

            System.out.println(
                    "Reason: " + e.getMessage()
            );

            return false;
        }
    }
}
