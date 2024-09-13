package com.example.historian.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqliteDate {
  private Date date;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public Date getDate() {
    return date;
  }

  public SqliteDate(Date date) {
    this.date = date;
  }

  public SqliteDate(String sqliteFormat) throws ParseException {
    if (sqliteFormat == null) {
      this.date = null;
    } else {
      this.date = dateFormat.parse(sqliteFormat);
    }
  }

  public String toSqliteFormat() {
    if (this.date == null) return null;
    return dateFormat.format(date);
  }
}
