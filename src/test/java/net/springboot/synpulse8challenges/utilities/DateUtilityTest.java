package net.springboot.synpulse8challenges.utilities;

import net.springboot.synpulse8challenges.constants.DateFormatConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtilityTest {

    @Test
    public void testCheckDateFormatValid() {
        boolean result = DateUtility.checkDateFormatValid("123", DateFormatConstants.DATE_FORMAT_DD_MM_YYYY);
        Assertions.assertFalse(result);
        result = DateUtility.checkDateFormatValid("01-01-2022", DateFormatConstants.DATE_FORMAT_DD_MM_YYYY);
        Assertions.assertTrue(result);
    }

    @Test
    public void testParseDateFromString() {
        Date result = DateUtility.parseDateFromString("123", DateFormatConstants.DATE_FORMAT_DD_MM_YYYY);
        Assertions.assertNull(result);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatConstants.DATE_FORMAT_DD_MM_YYYY);
        try {
            Date testDate = simpleDateFormat.parse("01-01-2022");
            result = DateUtility.parseDateFromString("01-01-2022", DateFormatConstants.DATE_FORMAT_DD_MM_YYYY);

            Assertions.assertEquals(testDate, result);
        } catch (Exception ex) {
            Assertions.fail("Failing in testParseDateFromString");
        }

    }
}
