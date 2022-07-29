/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.graphics.Rect;
import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(sdk= Build.VERSION_CODES.N)
@RunWith(RobolectricTestRunner.class)
public class RectUtilsTest {
    @Test
    public void testIntersectionContained() {
        final Rect container = new Rect(0, 0, 300, 600);
        Rect view = new Rect(50, 25, 150, 500);
        Rect intersect = RectUtils.intersection(container, view);
        assertEquals(50, intersect.left);
        assertEquals(25, intersect.top);
        assertEquals(150, intersect.right);
        assertEquals(500, intersect.bottom);
    }

    @Test
    public void testIntersectionAtEdge() {
        final Rect container = new Rect(0, 0, 300, 600);
        Rect view = new Rect(50, 25, 150, 600);
        Rect intersect = RectUtils.intersection(container, view);
        assertEquals(50, intersect.left);
        assertEquals(25, intersect.top);
        assertEquals(150, intersect.right);
        assertEquals(600, intersect.bottom);
    }

    @Test
    public void testIntersectionExtendsBeyond() {
        final Rect container = new Rect(0, 0, 300, 600);
        Rect view = new Rect(50, 25, 250, 650);
        Rect intersect = RectUtils.intersection(container, view);
        assertEquals(50, intersect.left);
        assertEquals(25, intersect.top);
        assertEquals(250, intersect.right);
        assertEquals(600, intersect.bottom);
    }

    @Test
    public void testIntersectionContainerStartsLower() {
        final Rect container = new Rect(2, 2, 1080, 1930);
        final Rect view = new Rect(0, 0, 825, 500);
        Rect intersect = RectUtils.intersection(container, view);
        assertEquals(2, intersect.left);
        assertEquals(2, intersect.top);
        assertEquals(825, intersect.right);
        assertEquals(500, intersect.bottom);
    }

    @Test
    public void testNoIntersection() {
        final Rect container = new Rect(0, 0, 300, 600);
        Rect view = new Rect(300, 600, 350, 650);
        Rect intersect = RectUtils.intersection(container, view);
        assertNull(intersect);
    }

    @Test
    public void test99Intersect() {
        final Rect container = new Rect(0, 2, 1080, 1930);
        final Rect view = new Rect(0, 0, 825, 500);
        assertEquals(99, percentageContains(container, view));
    }

    @Test
    public void test100Intersect() {
        final Rect container = new Rect(0, 0, 1080, 1930);
        final Rect view = new Rect(0, 0, 825, 500);
        assertEquals(100, percentageContains(container, view));
    }

    @Test
    public void testExactIntersect() {
        final Rect container = new Rect(0, 0, 300, 600);
        final Rect view = new Rect(0, 0, 300, 600);
        assertEquals(100, percentageContains(container, view));
    }

    @Test
    public void test50PctContains() {
        final Rect frame = new Rect(0, 0, 300, 600);
        assertEquals(50, percentageContains(frame, new Rect( 150, 300, 450, 600)));
    }

    @Test
    public void test25PctContains() {
        final Rect frame = new Rect(0, 0, 300, 600);
        assertEquals(25, percentageContains(frame, new Rect(150, 450, 450, 750)));
    }

    @Test
    public void test16PctContains() {
        final Rect frame = new Rect(0, 0, 300, 600);
        assertEquals(16, percentageContains(frame, new Rect(150, 500, 450, 800)));
    }

    @Test
    public void test8PctContains() {
        final Rect frame = new Rect(0, 0, 300, 600);
        assertEquals(8, percentageContains(frame, new Rect(150, 500, 750, 800)));
    }

    @Test
    public void test4PctContains() {
        final Rect frame = new Rect(0, 0, 300, 600);
        assertEquals(4, percentageContains(frame, new Rect(150, 550, 750, 850)));
    }

    @Test
    public void testNoPctContains() {
        final Rect frame = new Rect(0, 0, 300, 600);
        assertEquals(0, percentageContains(frame, new Rect(300, 550, 900, 850)));
    }

    @Test
    public void testNoPct2Contains() {
        final Rect frame = new Rect(0, 0, 300, 600);
        assertEquals(0, percentageContains(frame, new Rect(150, 600, 750, 900)));
    }

    @Test
    public void testNoPct3Contains() {
        final Rect frame = new Rect(0, 0, 300, 600);
        assertEquals(0, percentageContains(frame, new Rect(299, 600, 899, 900)));
    }

    @Test
    public void testNoPct4Contains() {
        final Rect frame = new Rect(0, 0, 300, 600);
        assertEquals(1, percentageContains(frame, new Rect(299, 599, 899, 999)));
    }

    private int percentageContains(final Rect container, final Rect childView) {
        final Rect intersection = RectUtils.intersection(container, childView);
        if (intersection != null) {
            return RectUtils.percentageContains(childView, intersection);
        }
        return 0;
    }
}
