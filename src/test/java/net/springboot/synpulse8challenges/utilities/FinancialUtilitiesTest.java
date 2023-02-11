package net.springboot.synpulse8challenges.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FinancialUtilitiesTest {

    @Test
    public void testIsAmountDebit(){
        boolean result = FinancialUtilities.isAmountDebit(-1d);
        Assertions.assertTrue(result);
        result = FinancialUtilities.isAmountDebit(100d);
        Assertions.assertFalse(result);
    }
}
