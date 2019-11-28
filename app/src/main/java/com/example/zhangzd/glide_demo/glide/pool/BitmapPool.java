package com.example.zhangzd.glide_demo.glide.pool;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.example.zhangzd.glide_demo.glide.util.Tool;

import java.util.TreeMap;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-28 15:52
 */
public class BitmapPool extends LruCache<Integer,Bitmap> implements PoolInterface  {

    private TreeMap<Integer,String> keyMap = new TreeMap<>();

    public BitmapPool(int maxSize) {
        super(maxSize);

    }

    @Override
    protected int sizeOf(Integer key, Bitmap value) {
        return getBitmapSize(value);
    }

    private int getBitmapSize(Bitmap bitmap) {
        return bitmap.getAllocationByteCount();
    }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        int size = width * height * (config == Bitmap.Config.ARGB_8888?4 :2);
        Integer ceilingKey = keyMap.ceilingKey(size);
        if (ceilingKey == null) {
            return null; // 如果找不到 保存的key，就直接返回null，无法复用
        }


        if (ceilingKey <= size * 2) {
            Bitmap remove = remove(ceilingKey);
            return remove;
        }
        return null;
    }

    @Override
    public void put(Bitmap bitmap) {
        Tool.checkNotEmpty(bitmap);
        if (!bitmap.isMutable()) {
            if (bitmap.isRecycled() == false) {
                bitmap.recycle();
            }
            return;
        }
        int bitmapSize = getBitmapSize(bitmap);
        if (bitmapSize > maxSize()) {
            if (bitmap.isRecycled() == false) {
                bitmap.recycle();
            }
            return;
        }


        put(bitmapSize,bitmap);
        keyMap.put(bitmapSize,"");

    }

    @Override
    protected void entryRemoved(boolean evicted, Integer key, Bitmap oldValue, Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        keyMap.remove(key);
    }
}
