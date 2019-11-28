package com.example.zhangzd.glide_demo.glide;

import android.content.Context;

import com.example.zhangzd.glide_demo.glide.resource.ImgResource;

/**
 * 加载外部资源 标准
 */
public interface ILoadData {

    // 加载外部资源的行为
     ImgResource loadResource(String path, ResponseListener responseListener, Context context);

}
