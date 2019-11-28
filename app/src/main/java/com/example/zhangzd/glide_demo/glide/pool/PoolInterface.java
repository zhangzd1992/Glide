package com.example.zhangzd.glide_demo.glide.pool;

import android.graphics.Bitmap;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-28 15:57
 */
public interface PoolInterface {
    Bitmap get(int width, int height, Bitmap.Config config);
    void put(Bitmap bitmap);
}
