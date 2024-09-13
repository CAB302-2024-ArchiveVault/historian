package com.example.historian.models.account;

/**
 * Enum representing the different privilege levels of an account.
 */
public enum AccountPrivilege {
  DATABASE_OWNER("database_owner"),
  ADMIN("admin"),
  CURATOR("curator"),
  MEMBER("member"),
  VIEWER("viewer");

  private final String value;

  /**
   * Constructs an AccountPrivilege with the specified value.
   *
   * @param value the string representation of the account privilege
   */
  AccountPrivilege(String value) {
    this.value = value;
  }

  /**
   * Gets the string representation of the account privilege.
   *
   * @return the string representation of the account privilege
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns the string representation of the account privilege.
   *
   * @return the string representation of the account privilege
   */
  @Override
  public String toString() {
    return this.value;
  }

  /**
   * Returns the AccountPrivilege corresponding to the specified string value.
   *
   * @param value the string representation of the account privilege
   * @return the AccountPrivilege corresponding to the specified string value
   * @throws IllegalArgumentException if the specified value does not correspond to any AccountPrivilege
   */
  public static AccountPrivilege fromString(String value) {
    for (AccountPrivilege accountPrivilege : AccountPrivilege.values()) {
      if (accountPrivilege.value.equalsIgnoreCase(value)) {
        return accountPrivilege;
      }
    }
    throw new IllegalArgumentException("Unknown enum value: " + value);
  }
}
