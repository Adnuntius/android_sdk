/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data.profile;

import java.util.Calendar;
import java.util.TimeZone;

import static com.adnuntius.android.sdk.StringUtils.paddedString;

/**
 * For older android targets, can use this class instead of java.time.Instant
 */
public class Instant {
    private final Calendar cal;

    private Instant(final Calendar cal) {
        this.cal = cal;
    }

    public static Instant now() {
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return new Instant(cal);
    }

    public String toString() {
        return paddedString(cal.get(Calendar.YEAR), 4)
        + "-"
        + paddedString(cal.get(Calendar.MONTH) + 1, 2)
        + "-"
        + paddedString(cal.get(Calendar.DAY_OF_MONTH) + 1, 2)
        + "T"
        + paddedString(cal.get(Calendar.HOUR_OF_DAY) + 1, 2)
        + ":"
        + paddedString(cal.get(Calendar.MINUTE) + 1, 2)
        + ":"
        + paddedString(cal.get(Calendar.SECOND) + 1, 2)
        + "."
        + paddedString(cal.get(Calendar.MILLISECOND) + 1, 3)
        + "Z";
    }
}
