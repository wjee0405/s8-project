package net.springboot.synpulse8challenges.utilities;

import net.springboot.synpulse8challenges.model.Account;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.TransactionQuery;

public class DataProvider {

    public static Account prepareAccount(String accountOwner, String accountNo){
        Account account = new Account();
        account.setAccountOwner(accountOwner);
        account.setAccountNo(accountNo);

        return account;
    }

    public static TransactionQuery prepareTransactionQuery(
            String startDate, String endDate, String userId, String accountNo
    ){
        TransactionQuery transactionQuery = new TransactionQuery();
        transactionQuery.setTransactionStartDateValue(startDate);
        transactionQuery.setTransactionEndDateValue(endDate);
        transactionQuery.setUserId(userId);
        transactionQuery.setAccountNo(accountNo);

        return transactionQuery;
    }

    public static TransactionQuery prepareTransactionQuery(
            String startDate, String endDate, String userId, String accountNo,
            Integer pageIndex, Integer pageRecordCount
    ){
        TransactionQuery transactionQuery = prepareTransactionQuery(startDate,endDate,userId,accountNo);
        transactionQuery.setPageIndex(pageIndex);
        transactionQuery.setPageRecordCount(pageRecordCount);

        return transactionQuery;
    }


    public static Transaction prepareTransactionData(
            Double amount, String accountNo, String valueDate,String description){
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setAccountNo(accountNo);
        transaction.setValueDate(valueDate);
        transaction.setDescription(description);

        return transaction;
    }

    public static Transaction prepareTransactionData(
            Double amount, String accountNo, String valueDate,String description, String currencyAmount){
        Transaction transaction = prepareTransactionData(amount, accountNo, valueDate, description);
        transaction.setCurrencyAmount(currencyAmount);

        return transaction;
    }
}
