package com.example.historian.models.account;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;

public class Account {
  private int id;
  private String username;
  private byte[] passwordSalt;
  private String passwordHash;
  private AccountPrivilege accountPrivilege;

  public Account(String username, String password, AccountPrivilege accountPrivilege) throws Exception {
    this.username = username;
    this.passwordSalt = this.getSalt();
    this.passwordHash = this.hashPassword(password, this.passwordSalt);
    this.accountPrivilege = accountPrivilege;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return this.username;
  }

  public byte[] getPasswordSalt() {
    return this.passwordSalt;
  }

  public String getPasswordHash() {
    return this.passwordHash;
  }

  public AccountPrivilege getAccountPrivilege() {
    return accountPrivilege;
  }

  // Password hashing

  private byte[] getSalt() throws Exception {
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

  public boolean comparePassword(String otherPassword) throws Exception {
    String otherPasswordHashed = this.hashPassword(otherPassword, this.passwordSalt);
    return otherPasswordHashed.equals(this.passwordHash);
  }
}
