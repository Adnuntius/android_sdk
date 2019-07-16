package com.adnuntius.sdk.lib;

interface ICallback {
    void callback(String s);
}
public abstract class Callback implements ICallback {
    public AdView viewContext;
    public Callback(AdView ctx) {
        this.viewContext = ctx;
    }
}
