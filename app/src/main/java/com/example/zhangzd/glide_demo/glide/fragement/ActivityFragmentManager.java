package com.example.zhangzd.glide_demo.glide.fragement;

import android.annotation.SuppressLint;
import android.app.Fragment;

import com.example.zhangzd.glide_demo.glide.LifecycleCallback;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-28 12:03
 */
public class ActivityFragmentManager extends Fragment {
    private LifecycleCallback lifecycleCallback;
    public ActivityFragmentManager() {}
    @SuppressLint("ValidFragment")
    public ActivityFragmentManager(LifecycleCallback lifecycleCallback) {
        this.lifecycleCallback = lifecycleCallback;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (lifecycleCallback != null) {
            lifecycleCallback.glideInitAction();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (lifecycleCallback != null) {
            lifecycleCallback.glideStopAction();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lifecycleCallback != null) {
            lifecycleCallback.glideRecycleAction();
        }
    }
}
