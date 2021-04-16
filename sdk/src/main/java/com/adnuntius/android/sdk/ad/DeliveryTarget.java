package com.adnuntius.android.sdk.ad;

public enum DeliveryTarget {
    Impression("i");

    private final String target;

    private DeliveryTarget(final String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}
