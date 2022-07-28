/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PageTest {
    @Test
    public void testPageWithUrl() {
        Page page = new Page("http://google.com");
        assertEquals("google.com", page.getDomainName());
        assertTrue(page.getCategories().isEmpty());

        page = new Page("http://google.com/hello");
        assertEquals("google.com", page.getDomainName());
        assertEquals("hello", page.getCategories().get(0));

        page = new Page("http://google.com/hello/world");
        assertEquals("google.com", page.getDomainName());
        assertEquals("hello", page.getCategories().get(0));
        assertEquals("world", page.getCategories().get(1));
    }
}
