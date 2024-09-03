package com.example.historian.models.account;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Password {
  private byte[] salt;
  private String hash;

  public Password(byte[] salt, String hash) {
    this.salt = salt;
    this.hash = hash;
  }

  public Password(String encodedSalt, String hash) {
    this.salt = Base64.getDecoder().decode(encodedSalt);
    this.hash = hash;
  }

  public Password(String plainText) {
    try {
      this.salt = generateSalt();
      this.hash = hashPassword(plainText, salt);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getEncodedSalt() {
    return Base64.getEncoder().encodeToString(this.salt);
  }

  public String getHash() {
    return this.hash;
  }


  private byte[] generateSalt() throws Exception {
    SecureRandom sr = new SecureRandom();
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    return salt;
  }

  private String hashPassword(String password, byte[] salt) throws Exception {
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hash = factory.generateSecret(spec).getEncoded();
    return Base64.getEncoder().encodeToString(hash);
  }

  public boolean compare(String otherPassword) throws Exception {
    String otherPasswordHashed = this.hashPassword(otherPassword, this.salt);
    return otherPasswordHashed.equals(this.hash);
  }
}
