package com.adnuntius.android.sdk.http.volley;

import com.adnuntius.android.sdk.data.DataResponseHandler;
import com.adnuntius.android.sdk.http.ErrorResponse;
import com.adnuntius.android.sdk.http.HttpResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MockResponseHandler implements DataResponseHandler, HttpResponseHandler {
    final List<ErrorResponse> errorResponses = new ArrayList<>();
    final List<String> responses = new ArrayList<>();

    public void clear() {
        errorResponses.clear();
        responses.clear();
    }

    public void waitForMessages(long addedMessages) {
        while((responses.size() + errorResponses.size()) < addedMessages) {
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
    public void onSuccess(String response) {
        responses.add(response);
    }

    @Override
    public void onSuccess() {
        responses.add("");
    }
}
