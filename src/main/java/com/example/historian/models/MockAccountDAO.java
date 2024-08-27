package com.example.historian.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MockAccountDAO implements IAccountDAO {
  public static final ArrayList<Account> accounts = new ArrayList<>();
  private static int autoIncrementedId = 0;

  public MockAccountDAO() {
    addAccount(new Account("i_am_admin", "admin123", AccountPrivilege.ADMIN));
    addAccount(new Account("i_am_curator", "curator123", AccountPrivilege.CURATOR));
    addAccount(new Account("i_am_member", "member123", AccountPrivilege.MEMBER));
    addAccount(new Account("i_am_viewer", "viewer123", AccountPrivilege.VIEWER));
  }

  @Override
  public void addAccount(Account account) {
    account.setId(autoIncrementedId);
    autoIncrementedId++;
    accounts.add(account);
  }

  @Override
  public void updateAccount(Account account) {
    for (int i = 0; i < accounts.size(); i++) {
      if (accounts.get(i).getId() == account.getId()) {
        accounts.set(i, account);
        break;
      }
    }
  }

  @Override
  public void removeAccount(Account account) {
    accounts.remove(account);
  }

  @Override
  public Account getAccount(int accountId) {
    for (Account account : accounts) {
      if (account.getId() == accountId) {
        return account;
      }
    }
    return null;
  }

  @Override
  public Account getAccount(String username) {
    for (Account account : accounts) {
      if (Objects.equals(account.getUsername(), username)) {
        return account;
      }
    }
    return null;
  }

  @Override
  public List<Account> getAllAccounts() {
    return new ArrayList<>(accounts);
  }
}
