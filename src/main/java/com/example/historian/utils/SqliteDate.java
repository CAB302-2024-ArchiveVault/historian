package com.example.historian.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The SqliteDate class provides methods for converting between Date objects and SQLite date strings.
 */
public class SqliteDate {
  private Date date;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * Returns the Date object.
   *
   * @return the Date object
   */
  public Date getDate() {
    return date;
  }

  /**
   * Constructs a SqliteDate object with the specified Date object.
   *
   * @param date the Date object
   */
  public SqliteDate(Date date) {
    this.date = date;
  }

  /**
   * Constructs a SqliteDate object with the specified SQLite date string.
   *
   * @param sqliteFormat the SQLite date string
   * @throws ParseException if the date string cannot be parsed
   */
  public SqliteDate(String sqliteFormat) throws ParseException {
    if (sqliteFormat == null) {
      this.date = null;
    } else {
      this.date = dateFormat.parse(sqliteFormat);
    }
  }

  /**
   * Converts the Date object to an SQLite date string.
   *
   * @return the SQLite date string, or null if the date is null
   */
  public String toSqliteFormat() {
    if (this.date == null) return null;
    return dateFormat.format(date);
  }
}