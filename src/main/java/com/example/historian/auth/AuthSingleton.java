package com.example.historian.auth;

import com.example.historian.models.account.Account;

public class AuthSingleton {
  private static AuthSingleton instance = new AuthSingleton();
  private Account account;

  public static synchronized AuthSingleton getInstance() {
    return instance;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public boolean checkAuthorised() {
    return account != null;
  }

  public void signOut() {
    this.account = null;
  }
}
