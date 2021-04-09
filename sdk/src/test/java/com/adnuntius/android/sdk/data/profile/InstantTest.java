package com.adnuntius.android.sdk.data.profile;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class InstantTest {
    @Test
    public void testInstantToString() {
        final java.time.Instant jdkInstant = Instant.now();
        final Instant instant = Instant.now();
        assertEquals(instant.toString(), jdkInstant.toString());
    }
}
