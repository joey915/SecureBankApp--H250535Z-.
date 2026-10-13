import java.time.LocalDateTime;

public class Transaction {

    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private LocalDateTime dateTime;

    public Transaction(String transactionId,
                       String accountNumber,
                       String type,
                       double amount,
                       LocalDateTime dateTime) {

        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}