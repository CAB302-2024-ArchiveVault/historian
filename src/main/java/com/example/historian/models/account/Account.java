package com.example.historian.models.account;

import java.util.Objects;

public class Account {
  private int id;
  private String username;
  private String password;
  private AccountPrivilege accountPrivilege;

  public Account(String username, String password, AccountPrivilege accountPrivilege) {
    this.username = username;
    this.password = password;
    this.accountPrivilege = accountPrivilege;
  }

  public int getId() { return this.id; }
  public void setId(int id) { this.id = id; }
  public String getUsername() { return this.username; }
  public AccountPrivilege getAccountPrivilege() { return accountPrivilege; }

  public boolean comparePassword(String otherPassword) {
    return Objects.equals(otherPassword, this.password);
  }

  public void setAccountPrivilege(AccountPrivilege accountPrivilege) {
    this.accountPrivilege = accountPrivilege;
  }

  public void resetPassword(String password) {
    this.password = password;
  }
}
