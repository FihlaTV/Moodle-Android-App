package com.cyberthieves.trialapp;


import android.app.Application;

import java.net.CookieHandler;
import java.net.CookieManager;

public class CookieManage extends Application {
    CookieManager cookiemanage;

    public void onCreate() {
        cookiemanage = new CookieManager();
        CookieHandler.setDefault(cookiemanage);
        super.onCreate();
    }
}