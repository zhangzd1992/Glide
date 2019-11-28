package com.example.zhangzd.glide_demo.glide.resource;


/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 13:59
 */
public interface ImageNoUseCallback {
    /**
     * 资源不再使用时的回调方法
     * @param key
     * @param value
     */
    void valueNonUseListener(String key, ImgResource value);
}
