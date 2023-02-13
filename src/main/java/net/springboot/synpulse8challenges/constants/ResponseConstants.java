package net.springboot.synpulse8challenges.constants;

public interface ResponseConstants {
    String USERID_CANNOT_BE_NULL = "UserId cannot be empty";
    String USER_CREATION_SUCCESS = "User Creation Success";
    String USER_CREATION_FAIL = "User Creation Fail";
    String USER_NOT_FOUND = "Unable to find user. Please create user";
    String USER_EXISTS = "User already exists. Unable to create user";

    String ACCOUNT_CREATED = "Account created successfully";
    String ACCOUNT_NOT_FOUND = "No account is found for user";
    String ACCOUNT_FOUND = "Successfully retrieve account";

    String ACCOUNT_RETRIEVE_SUCCESSFUL = "Account retrieved successful";

    String CURRENCY_NOT_SUPPORTED = "Currency not supported. Please create account in other currency";

    String INSUFFICIENT_INPUT_PARAMETER = "Insufficient Input Parameter";
}
