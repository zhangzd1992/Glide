package com.example.zhangzd.glide_demo.glide.resource;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.zhangzd.glide_demo.glide.util.Tool;

/**
 * @Description: bitmap封装类
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 13:46
 */
public class ImgResource {
    private final String TAG = ImgResource.class.getSimpleName();
    private int count;   // 被引用的次数
    private Bitmap bitmap; //图片资源
    private String key;
    private ImageNoUseCallback noUseCallback;

    // 单利模式
    private static ImgResource value;

    public static ImgResource getInstance() {
        if (null == value) {
            synchronized (ImgResource.class) {
                if (null == value) {
                    value = new ImgResource();
                }
            }
        }
        return value;
    }

    //使用一次，计数加一
    public void useImage() {
        Tool.checkNotEmpty(bitmap);
        if (bitmap.isRecycled()) {
            Log.d(TAG, "useAction: 已经被回收了");
            return;
        }
        count++;
    }

    /**
     * 完成使用是调用，调用一次，计数减一
     */
    public void completeUse() {
        if (--count <= 0 && noUseCallback != null) {
            noUseCallback.valueNonUseListener(key,this);
        }
    }


    public void recycler() {
        if (count > 0) {
            Log.d(TAG, "recycleBitmap: 引用计数大于0，证明还在使用中，不能去释放...");
            return;
        }

        if (bitmap.isRecycled()) { // 被回收了
            Log.d(TAG, "recycleBitmap: mBitmap.isRecycled() 已经被释放了...");
            return;
        }

        bitmap.recycle();

        value = null;

        System.gc();
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ImageNoUseCallback getNoUseCallback() {
        return noUseCallback;
    }

    public void setNoUseCallback(ImageNoUseCallback noUseCallback) {
        this.noUseCallback = noUseCallback;
    }
}
