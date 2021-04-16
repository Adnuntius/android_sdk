package com.adnuntius.android.sdk.http.volley;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

public class TestHttpRequestQueue implements HttpRequestQueue {
    private class ImmediateExecutor implements Executor {
        private final AtomicLong count;

        public ImmediateExecutor(final AtomicLong count) {
            this.count = count;
        }
        @Override
        public void execute(Runnable command) {
            command.run();
            count.incrementAndGet();
        }
    }

    private class ImmediateResponseDelivery extends ExecutorDelivery {
        public ImmediateResponseDelivery(final ImmediateExecutor executor) {
            super(executor);
        }
    }

    private long addedMessages;
    private final AtomicLong count = new AtomicLong();
    private final ImmediateExecutor executor = new ImmediateExecutor(count);
    private final ImmediateResponseDelivery delivery = new ImmediateResponseDelivery(executor);
    private final RequestQueue queue;

    public TestHttpRequestQueue() {
        final HurlStack httpStack = new HurlStack();
        queue = new RequestQueue(new NoCache(), new BasicNetwork(httpStack), 3, delivery);
        queue.start();
    }

    @Override
    public <T> void add(Request<T> req) {
        addedMessages++;
        queue.add(req);
    }

    public void waitForMessages() {
        while(count.get() < addedMessages) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        }
    }
}