package com.example.historian.models.account;

public enum AccountPrivilege {
  ADMIN("admin"),
  CURATOR("curator"),
  MEMBER("member"),
  VIEWER("viewer");

  private final String value;

  AccountPrivilege(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  public static AccountPrivilege fromString(String value) {
    for (AccountPrivilege accountPrivilege : AccountPrivilege.values()) {
      if (accountPrivilege.value.equalsIgnoreCase(value)) {
        return accountPrivilege;
      }
    }
    throw new IllegalArgumentException("Unknown enum value: " + value);
  }
}
