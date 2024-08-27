package com.example.historian.models.account;

public class AccountDAOSingleton {
  private static AccountDAOSingleton instance;
  private IAccountDAO accountDAO;

  private AccountDAOSingleton() {
    accountDAO = new MockAccountDAO();
  }

  public static synchronized AccountDAOSingleton getInstance() {
    if (instance == null) {
      return new AccountDAOSingleton();
    }
    return instance;
  }

  public IAccountDAO getAccountDAO() {
    return accountDAO;
  }
}
