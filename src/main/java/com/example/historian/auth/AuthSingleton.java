package com.example.historian.auth;

import com.example.historian.models.account.Account;

/**
 * Singleton class to manage authentication state.
 */
public class AuthSingleton {
  // Single instance of AuthSingleton
  private static AuthSingleton instance = new AuthSingleton();
  private Account account;

  /**
   * Returns the single instance of AuthSingleton.
   *
   * @return the single instance of AuthSingleton
   */
  public static synchronized AuthSingleton getInstance() {
    return instance;
  }

  /**
   * Gets the current authenticated account.
   *
   * @return the current authenticated account, or null if no account is authenticated
   */
  public Account getAccount() {
    return account;
  }

  /**
   * Sets the authenticated account.
   *
   * @param account the account to set as authenticated
   */
  public void setAccount(Account account) {
    this.account = account;
  }

  /**
   * Checks if there is an authenticated account.
   *
   * @return true if an account is authenticated, false otherwise
   */
  public boolean checkAuthorised() {
    return account != null;
  }

  /**
   * Signs out the current authenticated account.
   */
  public void signOut() {
    this.account = null;
  }
}
