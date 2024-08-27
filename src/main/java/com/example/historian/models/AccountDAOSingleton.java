package com.example.historian.models;

public class AccountDAOSingleton {
  private static AccountDAOSingleton instance;
  private MockAccountDAO accountDAO;

  private AccountDAOSingleton() {
    accountDAO = new MockAccountDAO();
  }

  public static synchronized AccountDAOSingleton getInstance() {
    if (instance == null) {
      return new AccountDAOSingleton();
    }
    return instance;
  }

  public MockAccountDAO getAccountDAO() {
    return accountDAO;
  }
}
