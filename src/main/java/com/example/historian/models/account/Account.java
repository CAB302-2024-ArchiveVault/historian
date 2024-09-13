package com.example.historian.models.account;

/**
 * Represents an account with a username, password, and account privilege.
 */
public class Account {
  private int id;
  private final String username;
  private Password password;
  private AccountPrivilege accountPrivilege;

  /**
   * Constructs an Account with the specified username, password, and account privilege.
   *
   * @param username the username of the account
   * @param password the password of the account
   * @param accountPrivilege the privilege level of the account
   */
  public Account(String username, String password, AccountPrivilege accountPrivilege) {
    this.username = username;
    this.password = new Password(password);
    this.accountPrivilege = accountPrivilege;
  }

  /**
   * Constructs an Account with the specified username, password object, and account privilege.
   *
   * @param username the username of the account
   * @param password the Password object of the account
   * @param accountPrivilege the privilege level of the account
   */
  public Account(String username, Password password, AccountPrivilege accountPrivilege) {
    this.username = username;
    this.password = password;
    this.accountPrivilege = accountPrivilege;
  }

  /**
   * Gets the ID of the account.
   *
   * @return the ID of the account
   */
  public int getId() {
    return this.id;
  }

  /**
   * Sets the ID of the account.
   *
   * @param id the new ID of the account
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the username of the account.
   *
   * @return the username of the account
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * Gets the password of the account.
   *
   * @return the Password object of the account
   */
  public Password getPassword() {
    return this.password;
  }

  /**
   * Gets the account privilege of the account.
   *
   * @return the account privilege of the account
   */
  public AccountPrivilege getAccountPrivilege() {
    return accountPrivilege;
  }

  /**
   * Sets the account privilege of the account.
   *
   * @param accountPrivilege the new account privilege of the account
   */
  public void setAccountPrivilege(AccountPrivilege accountPrivilege) {
    this.accountPrivilege = accountPrivilege;
  }

  /**
   * Resets the password of the account.
   *
   * @param password the new password of the account
   */
  public void resetPassword(String password) {
    this.password = new Password(password);
  }
}
