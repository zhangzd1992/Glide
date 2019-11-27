package com.example.zhangzd.glide_demo.glide;

import android.content.Context;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 17:18
 */
public class GlideBuilder {
    public Glide build() {
        RequestManagerRetriever retriever = new RequestManagerRetriever();
        return  new Glide(retriever);
    }
}
