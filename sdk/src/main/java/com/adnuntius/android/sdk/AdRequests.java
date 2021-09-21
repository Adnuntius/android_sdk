package com.adnuntius.android.sdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Only used for deprecated loadFromApi
 */
@Deprecated
class AdRequests {
    private List<AdRequest> adUnits = new ArrayList<>();

    List<AdRequest> getAdUnits() {
        return adUnits;
    }
}
