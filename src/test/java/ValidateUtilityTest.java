import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.constants.TransactionConstants;
import net.springboot.synpulse8challenges.kafka.AccountOpsImpl;
import net.springboot.synpulse8challenges.kafka.UserOps;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.utilities.ValidateUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Log4j2
public class ValidateUtilityTest {
    @Mock
    AccountOpsImpl accountOpsImps;
    @Mock
    UserOps userOps;
    @InjectMocks
    ValidateUtility validateUtility;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateTransaction(){
        when(accountOpsImps.findCurrencyAccount(anyString())).thenReturn(Boolean.TRUE);
        Transaction dummyData = prepareTransactionData(null,"111","11-11-2022","123");
        List<String> result = validateUtility.validateTransaction(dummyData);
        Assertions.assertEquals(TransactionConstants.ERROR_AMOUNT_UNDEFINED,result.get(0));
    }

    private Transaction prepareTransactionData(
            Double amount, String accountNo, String valueDate,String description){
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setAccountNo(accountNo);
        transaction.setValueDate(valueDate);
        transaction.setDescription(description);

        return transaction;
    }

}
