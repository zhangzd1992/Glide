package com.example.zhangzd.glide_demo.glide.cache;

import com.example.zhangzd.glide_demo.glide.resource.ImgResource;

/**
 * @Description: lru自动移除资源时回调（手动调用remove不响应）
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 15:48
 */
public interface MemoryCacheAutoRemoveCallback {
    ImgResource MemoryCacheAutoRemove(String key ,ImgResource value);
}
