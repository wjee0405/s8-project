package net.springboot.synpulse8challenges.constants;

public interface TransactionConstants {
    String ACCOUNT_NO = "accountNo";
    String TRANSACTION_DATE = "transactionDate";

    String TRANSACTION_SUCCESSFUL = "Transaction has ben sent successfully";
    String TRANSACTION_QUERY_SUCCESSFUL = "Transaction query successful";
    String TRANSACTION_QUERY_NOT_FOUND = "No transaction for found";

    String ERROR_AMOUNT_UNDEFINED = "Missing Payment Amount";
    String ERROR_ACCOUNT_UNDEFINED = "Missing Payment Account";
    String ERROR_ACCOUNT_NOT_FOUND = "Payment Account not found";
    String ERROR_TRANSACTION_DATE_UNDEFINED = "Missing Transaction Date";
    String ERROR_TRANSACTION_DATE_FORMAT_INVALID = "Transaction Date Format Invalid. Please use DD-MM-YYYY format.";
    String ERROR_DESCRIPTION_UNDEFINED = "Missing Payment Description";

    String ERROR_QUERY_USERID_MISSING = "Missing user account";
    String ERROR_QUERY_ACCOUNT_MISSING = "Missing account number";
    String ERROR_QUERY_USERID_NOT_FOUND = "Unable to find user account";
    String ERROR_QUERY_ACCOUNT_NOT_FOUND = "Unable to find account";
    String ERROR_QUERY_USER_ACCOUNT_NOT_FOUND = "Unable to find currency account associated with user";
    String ERROR_QUERY_DATE_FORMAT_INVALID = "Query Date Format Invalid. Please use DD-MM-YYYY format";
    String ERROR_QUERY_DATE_STARTDATE_MISSING = "Missing query Start Date";
    String ERROR_QUERY_DATE_ENDDATE_MISSING = "Missing query End Date";
}
