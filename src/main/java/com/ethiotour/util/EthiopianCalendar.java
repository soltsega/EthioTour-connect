package com.ethiotour.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class EthiopianCalendar {
    private static final Map<String, LocalDate> ethiopianHolidays = new HashMap<>();
    
    static {
        // Initialize major Ethiopian holidays (these would be calculated properly in production)
        ethiopianHolidays.put("Meskel", LocalDate.of(2026, 9, 27));
        ethiopianHolidays.put("Timkat", LocalDate.of(2026, 1, 19));
        ethiopianHolidays.put("Enkutatash", LocalDate.of(2026, 9, 11));
        ethiopianHolidays.put("Mawlid", LocalDate.of(2026, 9, 4));
    }
    
    public static LocalDate convertToGregorian(int ethiopianYear, int ethiopianMonth, int ethiopianDay) {
        // Simplified conversion - in production this would be more accurate
        int gregorianYear = ethiopianYear + 8;
        if (ethiopianMonth > 9 || (ethiopianMonth == 9 && ethiopianDay >= 11)) {
            gregorianYear--;
        }
        
        int gregorianMonth = ethiopianMonth + 2;
        if (gregorianMonth > 12) {
            gregorianMonth -= 12;
        }
        
        return LocalDate.of(gregorianYear, gregorianMonth, ethiopianDay);
    }
    
    public static int[] convertToEthiopian(LocalDate gregorianDate) {
        // Simplified conversion - in production this would be more accurate
        int ethiopianYear = gregorianDate.getYear() - 8;
        int ethiopianMonth = gregorianDate.getMonthValue() - 2;
        if (ethiopianMonth < 1) {
            ethiopianMonth += 12;
            ethiopianYear++;
        }
        
        return new int[]{ethiopianYear, ethiopianMonth, gregorianDate.getDayOfMonth()};
    }
    
    public static String getEthiopianDateDisplay(LocalDate gregorianDate) {
        int[] ethiopianDate = convertToEthiopian(gregorianDate);
        String[] ethiopianMonths = {
            "መስከረም", "ጥቅምት", "ህዳር", "ታህሳስ", 
            "ጥር", "የካቲት", "መጋቢት", "ሚያዝያ", 
            "ግንቦት", "ሰኔ", "ሃምሌ", "ነሃሴ"
        };
        
        return ethiopianDate[2] + " " + ethiopianMonths[ethiopianDate[1] - 1] + " " + ethiopianDate[0];
    }
    
    public static boolean isEthiopianHoliday(LocalDate date) {
        return ethiopianHolidays.containsValue(date);
    }
    
    public static String getHolidayName(LocalDate date) {
        for (Map.Entry<String, LocalDate> entry : ethiopianHolidays.entrySet()) {
            if (entry.getValue().equals(date)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static boolean isPeakSeason(LocalDate date) {
        // Peak seasons around major holidays and festivals
        int month = date.getMonthValue();
        return (month >= 9 && month <= 11) || (month >= 1 && month <= 3);
    }
    
    public static long getDaysUntilHoliday(LocalDate date, String holidayName) {
        LocalDate holiday = ethiopianHolidays.get(holidayName);
        if (holiday != null) {
            return ChronoUnit.DAYS.between(date, holiday);
        }
        return -1;
    }
}
