package com.ddz.demo.core.utils.time;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class YearMonthUtil {

    public static YearMonth parse(String text, String formatterStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterStr);
        return YearMonth.parse(text, formatter);
    }
}
