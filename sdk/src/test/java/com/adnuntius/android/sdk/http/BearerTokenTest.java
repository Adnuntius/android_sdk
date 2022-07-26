/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BearerTokenTest {
    @Test
    public void testTokenExpired() {
        final long currentTime = System.currentTimeMillis();

        BearerToken token = new BearerToken(currentTime - 3600 * 1000,
                "jason@pellcorp.com", "password", "asdasd123123123", 3600L, "asdadasdasd");
        assertTrue(token.isExpired(currentTime));

        token = new BearerToken(currentTime - 3601 * 1000,
                "jason@pellcorp.com", "password", "asdasd123123123", 3600L, "asdadasdasd");
        assertTrue(token.isExpired(currentTime));

        token = new BearerToken(currentTime - 3599 * 1000,
                "jason@pellcorp.com", "password", "asdasd123123123", 3600L, "asdadasdasd");
        assertFalse(token.isExpired(currentTime));
    }
}
