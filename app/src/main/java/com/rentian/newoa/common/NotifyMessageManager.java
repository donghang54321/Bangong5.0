package com.rentian.newoa.common;

/**
 * Created by rentianjituan on 2017/11/23.
 */

public class NotifyMessageManager {

    private static NotifyMessageManager instance;

    public static NotifyMessageManager getInstance() {
        if (instance == null) {
            instance = new NotifyMessageManager();
        }
        return instance;
    }

    private NotifyMessage listener;

    public void setNotifyMessage(NotifyMessage nm) {
        listener = nm;
    }

    public void sendNotifyMessage(String msg) {
        listener.sendMessage(msg);
    }

}
