package com.example.zhangzd.glide_demo.glide;

import com.example.zhangzd.glide_demo.glide.resource.ImgResource;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-28 15:10
 */
public interface ResponseListener {
     void responseSuccess(ImgResource value);
     void responseException(Exception e);
}
