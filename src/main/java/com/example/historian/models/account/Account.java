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
  private Password password;
  private AccountPrivilege accountPrivilege;

  public Account(String username, String password, AccountPrivilege accountPrivilege) {
    this.username = username;
    this.password = new Password(password);
    this.accountPrivilege = accountPrivilege;
  }

  public Account(String username, Password password, AccountPrivilege accountPrivilege) {
    this.username = username;
    this.password = password;
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

  public Password getPassword() {
    return this.password;
  }

  public AccountPrivilege getAccountPrivilege() {
    return accountPrivilege;
  }
}
