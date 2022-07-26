/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data.profile;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class InstantTest {
    @Test
    public void testInstantToString() {
        final java.time.Instant jdkInstant = Instant.now();
        final Instant instant = Instant.now();
        // the last value of the MS does not alway match, its close enough
        assertEquals(instant.toString().substring(0, 24), jdkInstant.toString().substring(0, 24));
    }
}
