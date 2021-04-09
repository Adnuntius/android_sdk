package com.adnuntius.android.sdk.http.volley;

import com.adnuntius.android.sdk.data.DataResponseHandler;
import com.adnuntius.android.sdk.http.ErrorResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MockHttpResponseHandler implements DataResponseHandler {
    final List<ErrorResponse> errorResponses = new ArrayList<>();
    final AtomicInteger responses = new AtomicInteger();

    public void clear() {
        errorResponses.clear();
        responses.set(0);
    }

    public void waitForMessages(long addedMessages) {
        while((responses.get() + errorResponses.size()) < addedMessages) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public void onFailure(ErrorResponse response) {
        errorResponses.add(response);
    }

    @Override
    public void onSuccess() {
        responses.incrementAndGet();
    }
}
