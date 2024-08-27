package com.example.historian.models;

import java.util.List;

public interface IAccountDAO {
  public void addAccount(Account account);

  public void updateAccount(Account account);

  public void removeAccount(Account account);

  public Account getAccount(int accountId);

  public List<Account> getAllAccounts();
}
