package net.springboot.synpulse8challenges.utilities;

import net.springboot.synpulse8challenges.constants.DateFormatConstants;
import net.springboot.synpulse8challenges.constants.TransactionConstants;
import net.springboot.synpulse8challenges.service.AccountOps;
import net.springboot.synpulse8challenges.service.UserOps;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.TransactionQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidateUtility {
    @Autowired
    AccountOps accountOps;
    @Autowired
    UserOps userOps;

    public List<String> validateTransaction(Transaction transaction) {
        List<String> errorMessage = new ArrayList<>();
        if (ObjectUtils.isEmpty(transaction.getAmount())) {
            errorMessage.add(TransactionConstants.ERROR_AMOUNT_UNDEFINED);
        }
        if (StringUtils.isEmpty(transaction.getAccountNo())) {
            errorMessage.add(TransactionConstants.ERROR_ACCOUNT_UNDEFINED);
        } else {
            if (!accountOps.findCurrencyAccount(transaction.getAccountNo())) {
                errorMessage.add(TransactionConstants.ERROR_ACCOUNT_NOT_FOUND);
            }
        }
        if (StringUtils.isEmpty(transaction.getValueDate())) {
            errorMessage.add(TransactionConstants.ERROR_TRANSACTION_DATE_UNDEFINED);
        } else {
            if (!DateUtility.checkDateFormatValid(transaction.getValueDate(), DateFormatConstants.DATE_FORMAT_DD_MM_YYYY)) {
                errorMessage.add(TransactionConstants.ERROR_TRANSACTION_DATE_FORMAT_INVALID);
            }
        }
        if (StringUtils.isEmpty(transaction.getDescription())) {
            errorMessage.add(TransactionConstants.ERROR_DESCRIPTION_UNDEFINED);
        }
        return errorMessage;
    }

    public List<String> validateTransactionQueries(TransactionQuery query){
        List<String> errorMessage = new ArrayList<>();
        if(!StringUtils.isEmpty(query.getTransactionStartDateValue())){
            if(!DateUtility.checkDateFormatValid(query.getTransactionStartDateValue(), DateFormatConstants.DATE_FORMAT_DD_MM_YYYY)){
                errorMessage.add(TransactionConstants.ERROR_QUERY_DATE_FORMAT_INVALID);
            }
            if(StringUtils.isEmpty(query.getTransactionEndDateValue())){
                errorMessage.add(TransactionConstants.ERROR_QUERY_DATE_ENDDATE_MISSING);
            }
        }

        if(!StringUtils.isEmpty(query.getTransactionEndDateValue())){
            if(!DateUtility.checkDateFormatValid(query.getTransactionEndDateValue(), DateFormatConstants.DATE_FORMAT_DD_MM_YYYY)){
                errorMessage.add(TransactionConstants.ERROR_QUERY_DATE_FORMAT_INVALID);
            }
            if(StringUtils.isEmpty(query.getTransactionStartDateValue())){
                errorMessage.add(TransactionConstants.ERROR_QUERY_DATE_STARTDATE_MISSING);
            }
        }

        if(!StringUtils.isEmpty(query.getUserId())){
            if(!userOps.findUser((query.getUserId()))){
                errorMessage.add(TransactionConstants.ERROR_QUERY_USERID_NOT_FOUND);
            }
        }
        if(!StringUtils.isEmpty(query.getAccountNo())){
            if(!accountOps.findCurrencyAccount(query.getAccountNo())){
                errorMessage.add(TransactionConstants.ERROR_QUERY_ACCOUNT_NOT_FOUND);
            }
        }

        return errorMessage;
    }
}
