package com.capsule.corp.infrastructure.http.resources;

public class Constants {
  // Error Messages
  public static final String CLIENT_NOT_FOUND_MESSAGE = "Client Not Found";
  public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account Not Found";
  public static final String NO_CHANGES_DETECTED_MESSAGE = "No Changes Detected";

  public static final String INVALID_AGE_MESSAGE = "Invalid Age";
  public static final String INVALID_EMAIL_MESSAGE = "Invalid ID Number";
  public static final String PRESENT_CLIENT_MESSAGE = "Client is Present";
  public static final String PRESENT_ACCOUNT_MESSAGE = "Account is Present";
  public static final String INVALID_ID_NUMBER_MESSAGE = "Invalid ID Number";
  public static final String CANNOT_COMMUNICATE_MESSAGE = "Cannot Communicate";
  public static final String INVALID_CELLPHONE_NUMBER_MESSAGE = "Invalid ID Number";
  public static final String INVALID_CLIENT_STATUS_MESSAGE = "Invalid Client Status";
  public static final String INVALID_ACCOUNT_STATUS_MESSAGE = "Invalid Account Status";

  // Patterns
  public static final String SA_ID_NUMBER_PATTERN =
      "^(\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{7}$";
  public static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  public static final String CELLPHONE_NUMBER_PATTERN = "^(\\+27|0)(6|7|8)[0-9]{8}$";
}
