package net.springboot.synpulse8challenges.utilities;

import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public class DateUtility {

    public static boolean checkDateFormatValid(String date, String format){
        boolean result = Boolean.TRUE;
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(date);
        }catch (Exception ex){
            log.error("Fail to check if date is valid",ex);
            result = Boolean.FALSE;
        }
        return result;
    }

    public static Date parseDateFromString(String date, String format){
        Date result = null;
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            result = simpleDateFormat.parse(date);
        }catch(Exception ex){
            log.error("Fail to parse date",ex);
        }
        return result;
    }
}
