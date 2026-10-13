Title: Secure Banking Application

Description;
This SecureBankApp is a Java based console banking application that is designed to provide simple banking services securely.
The application allows creation of a user account and login securely.
After a user logs in, the user can also deposit, withdraw, do balance enquiries and view transaction history.

Student Details: Joel Matuso H250535Z

Features;
**User Authentication**:
User login using username, account number, and password.
Password hashing to avoid storing passwords in plain text.
Generic error messages for invalid login credentials.
Collects all login credentials before displaying an authentication failure.
Failed-login handling.

**Account Management**:
Creation of new bank accounts.
Automatic generation of unique account numbers.
Viewing account balances.
Linking user details to their bank accounts.

**Banking Transactions**:
Depositing money into an account.
Withdrawing money from an account.
Prevention of withdrawals that exceed the available balance.
Recording transaction details.
Maintaining transaction history.

**File-Based Data Persistence**:
Storing user information in users.txt.
Storing account information in accounts.txt.
Storing transaction records in transactions.txt.
Loading saved data when the application starts.
Saving changes so that account information and transactions persist between sessions.

**Data Encryption**:
Implemented AES encryption using GCM mode (AES-GCM) to protect stored data.
Encrypting user, account, and transaction data files.
Using a separate encryption.key file to store the encryption key.
Using a 256-bit encryption key.
Using a 12-byte initialization vector (IV).
Using a 128-bit authentication tag to help detect unauthorized changes to encrypted data.

**Password Security**:
Using password hashing instead of storing passwords as readable text.
Verifying passwords during login.
Separating password-hashing functionality into PasswordHasher.java.

**Input Validation**:
Validating user input to reduce invalid or unexpected entries.
Validating transaction amounts.
Checking transaction conditions before processing deposits and withdrawals.

**File Security**:
Applying Windows file attributes to hide encryption.key.
Configuring file permissions using Windows access-control commands.
Separating encryption functionality into EncryptionUtil.java.
Separating file operations into FileManager.java.

**Object-Oriented Programming**:
Using separate Java classes to organize the application.
Main.java — application entry point and console interaction.
User.java — user information.
Account.java — bank account information and operations.
Transaction.java — transaction records.
PasswordHasher.java — password hashing and verification.
EncryptionUtil.java — data encryption and decryption.
FileManager.java — file storage and retrieval.

**Version Control and Project Submission**
Using Git for source-code version control.
Creating a .gitignore file to exclude compiled .class files and the .vscode/ directory.
Publishing the project to a public GitHub repository.
