/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import android.graphics.Rect;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RectUtils {
    public static Rect intersection(final Rect container, final Rect childView) {
        if (container.intersect(childView)) {
            int left = Math.max(childView.left, container.left);
            int top = Math.max(childView.top, container.top);
            int right = Math.min(childView.right, container.right);
            int bottom = Math.min(childView.bottom, container.bottom);
            return new Rect(left, top, right, bottom);
        } else {
            return null;
        }
    }

    public static int percentageContains(final Rect childView, final Rect intersection) {
        if (intersection.height() > 0 && intersection.width() > 0) {
            final int viewArea = childView.width() * childView.height();
            final int intersectArea = intersection.width() * intersection.height();
            final BigDecimal percentage = new BigDecimal(intersectArea / (double)(viewArea / 100));
            final int intPercentage = percentage.setScale(0, RoundingMode.DOWN).intValue();

            // a single pixel still must be valid, so just force to 1 percent
            return intPercentage > 0 ? intPercentage : 1;
        }
        return 0;
    }
}
