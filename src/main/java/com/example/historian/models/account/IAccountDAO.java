package com.example.historian.models.account;

import java.util.List;

/**
 * Interface for Data Access Object (DAO) to manage Account entities.
 */
public interface IAccountDAO {
  
  /**
   * Adds a new account to the data store.
   *
   * @param account the account to be added
   */
  public void addAccount(Account account);

  /**
   * Updates an existing account in the data store.
   *
   * @param account the account to be updated
   */
  public void updateAccount(Account account);

  /**
   * Removes an account from the data store.
   *
   * @param account the account to be removed
   */
  public void removeAccount(Account account);

  /**
   * Retrieves an account by its ID.
   *
   * @param accountId the ID of the account to be retrieved
   * @return the account with the specified ID, or null if not found
   */
  public Account getAccount(int accountId);

  /**
   * Retrieves an account by its username.
   *
   * @param username the username of the account to be retrieved
   * @return the account with the specified username, or null if not found
   */
  public Account getAccount(String username);

  /**
   * Retrieves all accounts from the data store.
   *
   * @return a list of all accounts
   */
  public List<Account> getAllAccounts();
}
