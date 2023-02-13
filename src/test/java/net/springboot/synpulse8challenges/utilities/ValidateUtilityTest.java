package net.springboot.synpulse8challenges.utilities;

import net.springboot.synpulse8challenges.constants.TransactionConstants;
import net.springboot.synpulse8challenges.service.AccountOps;
import net.springboot.synpulse8challenges.service.UserOps;
import net.springboot.synpulse8challenges.model.Transaction;
import net.springboot.synpulse8challenges.model.TransactionQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

public class ValidateUtilityTest {
    @Mock
    AccountOps accountOpsImps;
    @Mock
    UserOps userOps;
    @InjectMocks
    ValidateUtility validateUtility;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("transactionParameters")
    public void testValidateTransaction(Double amount, String accountNo, String valueDate, String description, List<String> output, Integer noOutput) {
        when(accountOpsImps.findCurrencyAccount("TRUE")).thenReturn(Boolean.TRUE);
        when(accountOpsImps.findCurrencyAccount("FALSE")).thenReturn(Boolean.FALSE);
        Transaction dummyData = DataProvider.prepareTransactionData(amount, accountNo, valueDate, description);
        List<String> result = validateUtility.validateTransaction(dummyData);
        Assertions.assertEquals(output, result);
        Assertions.assertEquals(noOutput, result.size());
    }

    @ParameterizedTest
    @MethodSource("transactionQueryParameters")
    public void testValidateTransactionQueries(String startDate, String endDate, String userId, String accountNo, List<String> output, Integer noOutput) {
        when(userOps.findUser("TRUE")).thenReturn(Boolean.TRUE);
        when(userOps.findUser("FALSE")).thenReturn(Boolean.FALSE);
        when(accountOpsImps.findCurrencyAccount("TRUE")).thenReturn(Boolean.TRUE);
        when(accountOpsImps.findCurrencyAccount("FALSE")).thenReturn(Boolean.FALSE);

        TransactionQuery dummyData = DataProvider.prepareTransactionQuery(startDate, endDate, userId, accountNo);
        List<String> result = validateUtility.validateTransactionQueries(dummyData);
        Assertions.assertEquals(output, result);
        Assertions.assertEquals(noOutput, result.size());
    }

    static Stream<Arguments> transactionParameters() {
        return Stream.of(
                Arguments.of(null, "TRUE", "01-01-2022", "2", Arrays.asList(TransactionConstants.ERROR_AMOUNT_UNDEFINED), 1),
                Arguments.of(111d, null, "01-01-2022", "2", Arrays.asList(TransactionConstants.ERROR_ACCOUNT_UNDEFINED), 1),
                Arguments.of(111d, "FALSE", "01-01-2022", "2", Arrays.asList(TransactionConstants.ERROR_ACCOUNT_NOT_FOUND), 1),
                Arguments.of(111d, "TRUE", null, "2", Arrays.asList(TransactionConstants.ERROR_TRANSACTION_DATE_UNDEFINED), 1),
                Arguments.of(111d, "TRUE", "01/01/2022", "2", Arrays.asList(TransactionConstants.ERROR_TRANSACTION_DATE_FORMAT_INVALID), 1),
                Arguments.of(111d, "TRUE", "01-01-2022", null, Arrays.asList(TransactionConstants.ERROR_DESCRIPTION_UNDEFINED), 1),
                Arguments.of(111d, "TRUE", "01-01-2022", "2", Arrays.asList(), 0)
        );
    }

    static Stream<Arguments> transactionQueryParameters() {
        return Stream.of(
                Arguments.of("01/01/2022", "01-01-2022", "TRUE", "TRUE", Arrays.asList(TransactionConstants.ERROR_QUERY_DATE_FORMAT_INVALID), 1),
                Arguments.of("01-01-2022", null, "TRUE", "TRUE", Arrays.asList(TransactionConstants.ERROR_QUERY_DATE_ENDDATE_MISSING), 1),
                Arguments.of("01-01-2022", "01/01/2022", "TRUE", "TRUE", Arrays.asList(TransactionConstants.ERROR_QUERY_DATE_FORMAT_INVALID), 1),
                Arguments.of(null, "01-01-2022", "TRUE", "TRUE", Arrays.asList(TransactionConstants.ERROR_QUERY_DATE_STARTDATE_MISSING), 1),
                Arguments.of("01-01-2022", "01-01-2022", "FALSE", "TRUE", Arrays.asList(TransactionConstants.ERROR_QUERY_USERID_NOT_FOUND), 1),
                Arguments.of("01-01-2022", "01-01-2022", "TRUE", "FALSE", Arrays.asList(TransactionConstants.ERROR_QUERY_ACCOUNT_NOT_FOUND), 1),
                Arguments.of("01-01-2022", "01-01-2022", "TRUE", "TRUE", Arrays.asList(), 0)
        );
    }


}
