package com.example.a41448.huawu.bean;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

public class Messages extends BmobIMMessageHandler {
    @Override
    public void onMessageReceive(MessageEvent event) {
        super.onMessageReceive(event);
    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent event) {
        super.onOfflineReceive(event);
    }
}
