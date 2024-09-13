package com.example.historian.models.account;

import com.example.historian.utils.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteAccountDAO implements IAccountDAO {
  private final Connection connection;

  public SqliteAccountDAO() {
    connection = SqliteConnection.getInstance();
//    createTable();
//    insertSampleData();
  }

  private void createTable() {
    try {
      Statement statement = connection.createStatement();
      String query = "CREATE TABLE IF NOT EXISTS accounts ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
              + "username VARCHAR NOT NULL,"
              + "passwordSalt VARCHAR NOT NULL,"
              + "passwordHash VARCHAR NOT NULL,"
              + "accountPrivilege VARCHAR NOT NULL"
              + ")";
      statement.execute(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void insertSampleData() {
    try {
      Statement clearStatement = connection.createStatement();
      String clearQuery = "DELETE FROM accounts";
      clearStatement.execute(clearQuery);

      addAccount(new Account("i_am_database_owner", "database_owner123", AccountPrivilege.DATABASE_OWNER));
      addAccount(new Account("i_am_admin", "admin123", AccountPrivilege.ADMIN));
      addAccount(new Account("i_am_curator", "curator123", AccountPrivilege.CURATOR));
      addAccount(new Account("i_am_member", "member123", AccountPrivilege.MEMBER));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Account createFromResultSet(ResultSet resultSet) throws Exception {
    int id = resultSet.getInt("id");
    String username = resultSet.getString("username");
    String passwordSalt = resultSet.getString("passwordSalt");
    String passwordHash = resultSet.getString("passwordHash");

    AccountPrivilege accountPrivilege = AccountPrivilege.fromString(resultSet.getString("accountPrivilege"));
    Password password = new Password(passwordSalt, passwordHash);
    Account account = new Account(username, password, accountPrivilege);
    account.setId(id);
    return account;
  }

  @Override
  public void addAccount(Account account) {
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (username, passwordSalt, passwordHash, accountPrivilege) VALUES (?, ?, ?, ?)");
      statement.setString(1, account.getUsername());
      statement.setString(2, account.getPassword().getEncodedSalt());
      statement.setString(3, account.getPassword().getHash());
      statement.setString(4, account.getAccountPrivilege().toString());
      statement.executeUpdate();

      // Set the ID of the new contact
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        account.setId(generatedKeys.getInt(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateAccount(Account account) {
    try {
      PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET username = ?, passwordSalt = ?, passwordHash = ?, accountPrivilege = ? WHERE id = ?");
      statement.setString(1, account.getUsername());
      statement.setString(2, account.getPassword().getEncodedSalt());
      statement.setString(3, account.getPassword().getHash());
      statement.setString(4, account.getAccountPrivilege().toString());
      statement.setInt(5, account.getId());
      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeAccount(Account account) {
    try {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts WHERE id = ?");
      statement.setInt(1, account.getId());
      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Account getAccount(int accountId) {
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE id = ?");
      statement.setInt(1, accountId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return createFromResultSet(resultSet);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Account getAccount(String username) {
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE username = ?");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return createFromResultSet(resultSet);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Account> getAllAccounts() {
    List<Account> accounts = new ArrayList<>();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts");
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        accounts.add(createFromResultSet(resultSet));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return accounts;
  }
}
