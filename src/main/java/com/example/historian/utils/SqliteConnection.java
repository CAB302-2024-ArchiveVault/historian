package com.example.historian.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A singleton class that facilitates a connection with the accounts SQL server.
 */
public class SqliteConnection {
  private static Connection instance = null;

  private SqliteConnection() {
    String url = "jdbc:sqlite:accounts.db";
    try {
      instance = DriverManager.getConnection(url);
    } catch (SQLException sqlEx) {
      System.err.println(sqlEx);
    }
  }

  /**
   * The method used to get the singleton instance that can be used to communicate with the accounts SQL server.
   * @return An instance of the SqliteConnection singleton
   */
  public static Connection getInstance() {
    if (instance == null) {
      new SqliteConnection();
    }
    return instance;
  }
}