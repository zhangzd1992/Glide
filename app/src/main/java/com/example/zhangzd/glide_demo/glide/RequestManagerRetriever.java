package com.example.zhangzd.glide_demo.glide;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 17:16
 */
public class RequestManagerRetriever {


    public RequestManager get(Activity activity) {
        return new RequestManager(activity);
    }

    public RequestManager get(FragmentActivity activity) {
        return new RequestManager(activity);
    }


    public RequestManager get(Context context) {
        return new RequestManager(context);
    }
}
