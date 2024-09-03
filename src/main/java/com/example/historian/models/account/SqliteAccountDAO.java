package com.example.historian.models.account;

import com.example.historian.utils.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class SqliteAccountDAO implements IAccountDAO {
  private Connection connection;

  public SqliteAccountDAO() {
    connection = SqliteConnection.getInstance();
    createTable();
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

  @Override
  public void addAccount(Account account) {
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (username, passwordSalt, passwordHash, accountPrivilege) VALUES (?, ?, ?, ?)");
      statement.setString(1, account.getUsername());
      statement.setString(2, account.getPasswordSalt().toString());
      statement.setString(2, account.getPasswordHash());
      statement.setString(3, account.getAccountPrivilege().toString());
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

  }

  @Override
  public void removeAccount(Account account) {

  }

  @Override
  public Account getAccount(int accountId) {
    return null;
  }

  @Override
  public Account getAccount(String username) {
    return null;
  }

  @Override
  public List<Account> getAllAccounts() {
    return List.of();
  }
}
