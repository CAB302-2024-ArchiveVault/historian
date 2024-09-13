package com.example.historian.models.account;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * The Password class provides methods for generating and verifying hashed passwords.
 */
public class Password {
  private byte[] salt;
  private String hash;

  /**
   * Constructs a Password object with the specified salt and hash.
   *
   * @param salt the salt as a byte array
   * @param hash the hashed password as a string
   */
  public Password(byte[] salt, String hash) {
    this.salt = salt;
    this.hash = hash;
  }

  /**
   * Constructs a Password object with the specified encoded salt and hash.
   *
   * @param encodedSalt the salt as a Base64 encoded string
   * @param hash the hashed password as a string
   */
  public Password(String encodedSalt, String hash) {
    this.salt = Base64.getDecoder().decode(encodedSalt);
    this.hash = hash;
  }

  /**
   * Constructs a Password object by hashing the specified plain text password.
   *
   * @param plainText the plain text password
   */
  public Password(String plainText) {
    try {
      this.salt = generateSalt();
      this.hash = hashPassword(plainText, salt);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the salt as a Base64 encoded string.
   *
   * @return the salt as a Base64 encoded string
   */
  public String getEncodedSalt() {
    return Base64.getEncoder().encodeToString(this.salt);
  }

  /**
   * Returns the hashed password.
   *
   * @return the hashed password
   */
  public String getHash() {
    return this.hash;
  }

  /**
   * Generates a random salt.
   *
   * @return a byte array containing the salt
   * @throws Exception if a SecureRandom instance cannot be created
   */
  private byte[] generateSalt() throws Exception {
    SecureRandom sr = new SecureRandom();
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    return salt;
  }

  /**
   * Hashes the specified password using the specified salt.
   *
   * @param password the plain text password
   * @param salt the salt
   * @return the hashed password as a Base64 encoded string
   * @throws Exception if the hashing algorithm is not available
   */
  private String hashPassword(String password, byte[] salt) throws Exception {
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hash = factory.generateSecret(spec).getEncoded();
    return Base64.getEncoder().encodeToString(hash);
  }

  /**
   * Compares the specified plain text password with the stored hashed password.
   *
   * @param otherPassword the plain text password to compare
   * @return true if the passwords match, false otherwise
   * @throws Exception if the hashing algorithm is not available
   */
  public boolean compare(String otherPassword) throws Exception {
    String otherPasswordHashed = this.hashPassword(otherPassword, this.salt);
    return otherPasswordHashed.equals(this.hash);
  }
}