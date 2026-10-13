public class User {

    private String username;
    private String passwordHash;
    private int failedLoginAttempts;
    private boolean locked;

    // Used when creating a new user
    public User(String username, String passwordHash) {

        this.username = username;
        this.passwordHash = passwordHash;
        this.failedLoginAttempts = 0;
        this.locked = false;
    }

    // Used when loading an existing user from the file
    public User(String username,
                String passwordHash,
                int failedLoginAttempts,
                boolean locked) {

        this.username = username;
        this.passwordHash = passwordHash;
        this.failedLoginAttempts = failedLoginAttempts;
        this.locked = locked;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public boolean isLocked() {
        return locked;
    }

    public void increaseFailedAttempts() {

        failedLoginAttempts++;

        if (failedLoginAttempts >= 3) {
            locked = true;
        }
    }

    public void resetFailedAttempts() {

        failedLoginAttempts = 0;
    }

    public void lockAccount() {

        locked = true;
    }
}