package net.springboot.synpulse8challenges.constants;

public interface TransactionConstants {
    String TRANSACTION_SUCCESSFUL = "Transaction has ben sent successfully";

    String ERROR_AMOUNT_UNDEFINED = "Missing Payment Amount";
    String ERROR_ACCOUNT_UNDEFINED = "Missing Payment Account";
    String ERROR_ACCOUNT_NOT_FOUND = "Payment Account not found";
    String ERROR_TRANSACTION_DATE_UNDEFINED = "Missing Transaction Date";
    String ERROR_TRANSACTION_DATE_FORMAT_INVALID = "Transaction Date Format Invalid. Please use DD-MM-YYYY format.";
    String ERROR_DESCRIPTION_UNDEFINED = "Missing Payment Description";
}
