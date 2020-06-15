package com.uni.julio.supertvplus.utils;

import android.content.Intent;

public class IntentUtil {
    public static final String TAG_TAGS = "tags";
    //for notification
    public static final String TAG_ACTION = "action";
    public static final String TAG_NOTIFICATION_TYPE = "notification_type";

    public static final String TAG_NOTIFICATION_ID = "NOTIFICATION_ID";

    public static void transportNotificationIntent(Intent current, Intent old){
        current.putExtra(IntentUtil.TAG_ACTION, old.getStringExtra(IntentUtil.TAG_ACTION));
        //current.putExtra(IntentUtil.TAG_NOTIFICATION_ID, old.getStringExtra(IntentUtil.TAG_NOTIFICATION_ID));
    }
}
