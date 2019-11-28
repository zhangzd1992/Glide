package com.example.zhangzd.glide_demo.glide;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;


/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 17:10
 */
public class Glide {
    private RequestManagerRetriever retriever;
    public Glide(RequestManagerRetriever retriever) {
        this.retriever = retriever;
    }

    public static RequestManager with(Activity activity) {
        return getRetriever().get(activity);
    }

    public static RequestManager with(FragmentActivity activity) {
        return getRetriever().get(activity);
    }

    public static RequestManager with(Context context) {
        return getRetriever().get(context);
    }

    private static RequestManagerRetriever getRetriever() {
        return get().retriever;
    }

    private static Glide get() {
        return  new GlideBuilder().build();
    }
}
