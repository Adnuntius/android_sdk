/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data.profile;

import java.util.Calendar;
import static com.adnuntius.android.sdk.StringUtils.paddedString;

/**
 * For older android targets, can use this class instead of java.time.LocalDate
 * for setting properties in the Profile class
 */
public class LocalDate {
    private final int year;
    private final int month;
    private final int dayOfMonth;

    private LocalDate(final int year, final int month, final int dayOfMonth) {
        if (year <= 0 || year > 9999) {
            throw new IllegalArgumentException("Illegal year value");
        }

        if (month <= 0 || month > 12) {
            throw new IllegalArgumentException("Illegal month value");
        }

        // im not validating date of month against the day, this is compatibility class
        // for very old android
        if (dayOfMonth <= 0 || dayOfMonth > 31) {
            throw new IllegalArgumentException("Illegal dayOfMonth value");
        }

        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public static LocalDate now() {
        final Calendar cal = Calendar.getInstance();
        return new LocalDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)
        );
    }

    public static LocalDate of(int year, int month, int dayOfMonth) {
        return new LocalDate(year, month, dayOfMonth);
    }

    public String toString() {
        return paddedString(year, 4)
                + "-"
                + paddedString(month, 2)
                + "-"
                + paddedString(dayOfMonth, 2);
    }
}
