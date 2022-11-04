package com.ulugames.uluandroidsdkdemo;

import android.app.Application;

import com.aliyun.sls.android.sdk.SLSDatabaseManager;

public class DemoApplication extends /*tw.com.mycard.sdk.libs.PSDK*/Application {//mycard包需要使用他们的application

    @Override
    public void onCreate() {
        super.onCreate();
        //阿里日志服务初始化
        SLSDatabaseManager.getInstance().setupDB(getApplicationContext());
    }
}
