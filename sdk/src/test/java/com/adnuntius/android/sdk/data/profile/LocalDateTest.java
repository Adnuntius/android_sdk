/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data.profile;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocalDateTest {
    @Test
    public void testToString() {
        LocalDate localDate = LocalDate.of(2017, 2, 23);
        assertEquals("2017-02-23", localDate.toString());

        java.time.LocalDate jdkLocalDate = java.time.LocalDate.of(2017, 2, 23);
        assertEquals("2017-02-23", jdkLocalDate.toString());
    }

    @Test
    public void testNow() {
        LocalDate localDate = LocalDate.now();
        java.time.LocalDate jdkLocalDate = java.time.LocalDate.now();
        assertEquals(localDate.toString(), jdkLocalDate.toString());
    }
}
