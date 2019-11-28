package com.example.zhangzd.glide_demo.glide;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.zhangzd.glide_demo.glide.fragement.ActivityFragmentManager;
import com.example.zhangzd.glide_demo.glide.fragement.FragmentActivityFragmentManager;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 17:11
 */
public class RequestManager {
    private static final int NEXT_HANDLER_MSG = 10000000;
    private Context requestManagetContext;
    private final String ACTIVITY_NAME = "activity_name";
    private RequestTargetEngine requestTargetEngine;

    {
        requestTargetEngine = new RequestTargetEngine();
    }

    public RequestManager(Activity activity) {
        requestManagetContext = activity;
        FragmentManager fragmentManager = activity.getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(ACTIVITY_NAME);
        if (fragment == null) {
            Fragment frag = new ActivityFragmentManager(requestTargetEngine);
            fragmentManager.beginTransaction().add(frag,ACTIVITY_NAME).commitAllowingStateLoss();
        }
        // 发送一次Handler
        mHandler.sendEmptyMessage(NEXT_HANDLER_MSG);
    }

    public RequestManager(Context context) {
        requestManagetContext = context;
    }

    public RequestManager(FragmentActivity activity) {
        requestManagetContext = activity;
        androidx.fragment.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
        androidx.fragment.app.Fragment fragment = fragmentManager.findFragmentByTag(ACTIVITY_NAME);
        if (fragment == null) {
            androidx.fragment.app.Fragment frag = new FragmentActivityFragmentManager(requestTargetEngine);
            fragmentManager.beginTransaction().add(frag,ACTIVITY_NAME).commitAllowingStateLoss();
        }
        // 发送一次Handler
        mHandler.sendEmptyMessage(NEXT_HANDLER_MSG);

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    public RequestTargetEngine load(String path) {
        mHandler.removeMessages(NEXT_HANDLER_MSG);
        requestTargetEngine.initValue(path,requestManagetContext);
        return requestTargetEngine;
    }
}
