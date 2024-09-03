package com.example.historian.models.account;

public class AccountDAOSingleton {
  private static AccountDAOSingleton instance;
  private IAccountDAO accountDAO;

  private AccountDAOSingleton() {
    try {
      accountDAO = new MockAccountDAO();
    } catch (Exception e) {
      e.printStackTrace();
    }
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
