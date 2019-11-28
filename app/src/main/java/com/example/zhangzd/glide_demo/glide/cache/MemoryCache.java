package com.example.zhangzd.glide_demo.glide.cache;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.example.zhangzd.glide_demo.glide.resource.ImgResource;

/**
 * @Description:  lru算法实现的lrucache,缓存图片资源
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 15:14
 */
public class MemoryCache  extends LruCache<String, ImgResource> {
    private boolean autoRemove = true;
    private MemoryCacheAutoRemoveCallback callback;
    public MemoryCache(int maxSize) {
        super(maxSize);
    }

    public void setCallback (MemoryCacheAutoRemoveCallback callback) {
        this.callback = callback;
    }

    @Override
    protected int sizeOf(String key, ImgResource value) {
        Bitmap bitmap = value.getBitmap();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return bitmap.getByteCount();
        }else {
            return bitmap.getAllocationByteCount();
        }

    }

    public ImgResource removeRes(String key) {
        autoRemove = false;
        ImgResource imgResource = remove(key);
        autoRemove = true;
        return imgResource;
    }



    @Override
    protected void entryRemoved(boolean evicted, String key, ImgResource oldValue, ImgResource newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        if (null != callback && autoRemove) {
            callback.MemoryCacheAutoRemove(key,oldValue);
        }
    }
}
