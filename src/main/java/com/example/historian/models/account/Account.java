package com.example.historian.models.account;

public class Account {
  private int id;
  private final String username;
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

  public void setAccountPrivilege(AccountPrivilege accountPrivilege) {
    this.accountPrivilege = accountPrivilege;
  }

  public void resetPassword(String password) {
    this.password = new Password(password);
  }
}
