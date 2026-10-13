import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {

    private static final String KEY_FILE = "encryption.key";

    private static final String ALGORITHM = "AES/GCM/NoPadding";

    private static final int KEY_SIZE = 256;

    private static final int IV_SIZE = 12;

    private static final int TAG_LENGTH = 128;


    // =========================
    // GET OR CREATE ENCRYPTION KEY
    // =========================

    private static SecretKey getKey() throws Exception {

        File file = new File(KEY_FILE);

        // Load existing key
        if (file.exists() && file.length() > 0) {

            byte[] encodedKey;

            try (FileInputStream input =
                         new FileInputStream(file)) {

                encodedKey = input.readAllBytes();
            }

            String keyString =
                    new String(
                            encodedKey,
                            StandardCharsets.UTF_8
                    );

            byte[] decodedKey =
                    Base64.getDecoder()
                            .decode(keyString);

            return new SecretKeySpec(
                    decodedKey,
                    "AES"
            );
        }


        // Generate a new AES key
        KeyGenerator keyGenerator =
                KeyGenerator.getInstance("AES");

        keyGenerator.init(KEY_SIZE);

        SecretKey key =
                keyGenerator.generateKey();


        // Convert key to Base64
        String encodedKey =
                Base64.getEncoder()
                        .encodeToString(
                                key.getEncoded()
                        );


        // Save key
        try (FileOutputStream output =
                     new FileOutputStream(file)) {

            output.write(
                    encodedKey.getBytes(
                            StandardCharsets.UTF_8
                    )
            );
        }

        return key;
    }


    // =========================
    // ENCRYPT DATA
    // =========================

    public static String encrypt(String data) {

        try {

            SecretKey key = getKey();


            // Generate a unique IV
            byte[] iv =
                    new byte[IV_SIZE];

            SecureRandom random =
                    new SecureRandom();

            random.nextBytes(iv);


            // Create GCM parameters
            GCMParameterSpec spec =
                    new GCMParameterSpec(
                            TAG_LENGTH,
                            iv
                    );


            // Create cipher
            Cipher cipher =
                    Cipher.getInstance(ALGORITHM);


            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    key,
                    spec
            );


            // Encrypt the data
            byte[] encrypted =
                    cipher.doFinal(
                            data.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );


            // Encode IV
            String encodedIV =
                    Base64.getEncoder()
                            .encodeToString(iv);


            // Encode encrypted data
            String encodedData =
                    Base64.getEncoder()
                            .encodeToString(
                                    encrypted
                            );


            // Store IV and encrypted data together
            return encodedIV + ":" + encodedData;

        } catch (Exception e) {

            System.out.println(
                    "Encryption error: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "Data encryption failed.",
                    e
            );
        }
    }


    // =========================
    // DECRYPT DATA
    // =========================

    public static String decrypt(
            String encryptedData) {

        try {

            if (encryptedData == null ||
                    encryptedData.trim().isEmpty()) {

                return "";
            }


            // Separate IV and encrypted data
            String[] parts =
                    encryptedData.split(
                            ":",
                            2
                    );


            if (parts.length != 2) {

                throw new IllegalArgumentException(
                        "Invalid encrypted data format."
                );
            }


            // Decode IV
            byte[] iv =
                    Base64.getDecoder()
                            .decode(parts[0]);


            // Decode encrypted data
            byte[] encrypted =
                    Base64.getDecoder()
                            .decode(parts[1]);


            // Get encryption key
            SecretKey key =
                    getKey();


            // Create GCM parameters
            GCMParameterSpec spec =
                    new GCMParameterSpec(
                            TAG_LENGTH,
                            iv
                    );


            // Create cipher
            Cipher cipher =
                    Cipher.getInstance(ALGORITHM);


            cipher.init(
                    Cipher.DECRYPT_MODE,
                    key,
                    spec
            );


            // Decrypt
            byte[] decrypted =
                    cipher.doFinal(
                            encrypted
                    );


            return new String(
                    decrypted,
                    StandardCharsets.UTF_8
            );

        } catch (Exception e) {

            System.out.println(
                    "Decryption error: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "Data decryption failed.",
                    e
            );
        }
    }
}