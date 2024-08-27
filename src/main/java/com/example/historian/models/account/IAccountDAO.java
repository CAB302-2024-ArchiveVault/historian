package com.example.historian.models.account;

import java.util.List;

public interface IAccountDAO {
  public void addAccount(Account account);

  public void updateAccount(Account account);

  public void removeAccount(Account account);

  public Account getAccount(int accountId);

  public Account getAccount(String username);

  public List<Account> getAllAccounts();
}
