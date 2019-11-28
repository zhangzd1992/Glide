package com.example.zhangzd.glide_demo.glide;

import android.content.Context;
import android.widget.ImageView;

import com.example.zhangzd.glide_demo.glide.cache.ActiveCache;
import com.example.zhangzd.glide_demo.glide.cache.DiskMemoryCache;
import com.example.zhangzd.glide_demo.glide.cache.MemoryCache;
import com.example.zhangzd.glide_demo.glide.cache.MemoryCacheAutoRemoveCallback;
import com.example.zhangzd.glide_demo.glide.pool.BitmapPool;
import com.example.zhangzd.glide_demo.glide.resource.ImageNoUseCallback;
import com.example.zhangzd.glide_demo.glide.resource.ImgResource;
import com.example.zhangzd.glide_demo.glide.resource.Key;
import com.example.zhangzd.glide_demo.glide.util.Tool;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 17:13
 */
public class RequestTargetEngine implements LifecycleCallback, ImageNoUseCallback, MemoryCacheAutoRemoveCallback,ResponseListener {

    private String path;
    private Context context;
    private String key;

    private ActiveCache activeCache; // 活动缓存
    private MemoryCache memoryCache; // 内存缓存
    private DiskMemoryCache diskLruCache; // 磁盘缓存
    private BitmapPool bitmapPool;     // bitmap 复用池
    private final int MEMORY_MAX_SIZE = 1024 * 1024 * 60;
    private ImageView imageView;

    public RequestTargetEngine() {
        if (activeCache == null) {
            activeCache = new ActiveCache(this);
        }

        if (memoryCache == null) {
            memoryCache = new MemoryCache(MEMORY_MAX_SIZE);
            memoryCache.setCallback(this);
        }

        if (diskLruCache == null)
            diskLruCache = new DiskMemoryCache();

        if(bitmapPool == null) {
            bitmapPool = new BitmapPool(MEMORY_MAX_SIZE);
        }

    }


    public void initValue(String path, Context requestManagetContext) {
        this.path = path;
        context = requestManagetContext;
        key = new Key(path).getKey();
    }


    public void into(ImageView imageView) {
        this.imageView = imageView;

        Tool.checkNotEmpty(imageView);
        //判断是否是在主线程
        Tool.assertMainThread();

        ImgResource value = cacheAction();
        if (null != value) {
            // 使用完成了 减一
            value.completeUse();
            imageView.setImageBitmap(value.getBitmap());
        }
    }

    private ImgResource cacheAction() {
        ImgResource imgResource = activeCache.get(key);
        if (imgResource != null) {
            imgResource.useImage();
            return imgResource;
        }

        imgResource = memoryCache.get(key);
        if (imgResource != null) {
            //从lru中手动移除，添加到活动缓存中
            memoryCache.removeRes(key);
            activeCache.put(key,imgResource);
            imgResource.useImage();
            return imgResource;
        }

        imgResource = diskLruCache.get(key,bitmapPool);
        if (imgResource != null) {
            activeCache.put(key,imgResource);
            imgResource.useImage();
            return imgResource;
        }

        // 磁盘缓存中也没有，则真正的进行网络请求

        imgResource = new LoadDataManager().loadResource(path, this, context);


        return imgResource;
    }

    @Override
    public void glideInitAction() {

    }

    @Override
    public void glideStopAction() {

    }

    @Override
    public void glideRecycleAction() {

    }


    /**
     * 资源不再使用时的回调方法
     *
     * @param key
     * @param value
     */
    @Override
    public void valueNonUseListener(String key, ImgResource value) {

    }

    @Override
    public ImgResource MemoryCacheAutoRemove(String key, ImgResource value) {
        // 内存缓存中的bitmap 被自动移除后添加到复用池，减少重复创建销毁bitmap，防治内存抖动



        return null;
    }

    @Override
    public void responseSuccess(ImgResource value) {
        if (null != value) {
            value.setKey(key);
            diskLruCache.put(key, value);
            imageView.setImageBitmap(value.getBitmap());
        }
    }

    @Override
    public void responseException(Exception e) {

    }
}
