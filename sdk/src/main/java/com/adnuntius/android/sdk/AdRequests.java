package com.adnuntius.android.sdk;

import java.util.ArrayList;
import java.util.List;

public class AdRequests {
    private List<AdRequest> adUnits = new ArrayList<>();

    public AdRequests() {

    }

    public AdRequests(AdRequest request) {
        adUnits.add(request);
    }

    List<AdRequest> getAdUnits() {
        return adUnits;
    }
}
