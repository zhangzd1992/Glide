package com.example.zhangzd.glide_demo.glide;

import android.app.Activity;


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

    private static RequestManagerRetriever getRetriever() {
        return get().retriever;
    }

    private static Glide get() {
        return  new GlideBuilder().build();
    }
}
