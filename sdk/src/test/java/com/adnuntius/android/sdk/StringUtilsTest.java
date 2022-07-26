/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StringUtilsTest {
    @Test
    public void testLocalDatePaddedString() {
        assertEquals("0001", StringUtils.paddedString("1", 4));
        assertEquals("0001", StringUtils.paddedString("0001", 4));
        assertEquals("0011", StringUtils.paddedString("11", 4));
        assertEquals("0111", StringUtils.paddedString("111", 4));
        assertEquals("1111", StringUtils.paddedString("1111", 4));
        assertEquals("11111", StringUtils.paddedString("11111", 4));

        assertEquals("0001", StringUtils.paddedString(1, 4));
        assertEquals("0011", StringUtils.paddedString(11, 4));
        assertEquals("0111", StringUtils.paddedString(111, 4));
        assertEquals("1111", StringUtils.paddedString(1111, 4));
        assertEquals("11111", StringUtils.paddedString(11111, 4));
    }
}
