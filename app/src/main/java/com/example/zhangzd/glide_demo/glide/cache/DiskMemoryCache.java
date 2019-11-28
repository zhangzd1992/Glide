package com.example.zhangzd.glide_demo.glide.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.zhangzd.glide_demo.glide.pool.BitmapPool;
import com.example.zhangzd.glide_demo.glide.resource.ImgResource;
import com.example.zhangzd.glide_demo.glide.cache.disk.DiskLruCache;
import com.example.zhangzd.glide_demo.glide.util.Tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description: 封装disklru 的相关操作
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 15:58
 */
public class DiskMemoryCache {
    private int appVersion = 1;
    private int valueCount = 1;
    private long maxSize = 1024 * 1024 *10;
    private DiskLruCache diskLruCache;

    private final String DISK_CACHE_DIR = "disk_lru_cache_dir";
    public DiskMemoryCache() {
        File file = new File(Environment.getRootDirectory() + File.separator + DISK_CACHE_DIR);
        try {
            diskLruCache = DiskLruCache.open(file, appVersion, valueCount, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void put(String key , ImgResource resource) {
        Tool.checkNotEmpty(key);
        DiskLruCache.Editor editor = null;
        OutputStream outputStream = null;
        try {
            editor = diskLruCache.edit(key);
            outputStream = editor.newOutputStream(0);
            Bitmap bitmap = resource.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (editor != null) {
                try {
                    editor.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public ImgResource get(String key, BitmapPool bitmapPool) {
        Tool.checkNotEmpty(key);
        DiskLruCache.Editor editor = null;
        InputStream inputStream = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                inputStream = snapshot.getInputStream(0);
                ImgResource imgResource = ImgResource.getInstance();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream,null,options);
                int width = options.outWidth;
                int height = options.outHeight;
                options.inMutable = true;
                options.inJustDecodeBounds = false;

                Bitmap bitmap = bitmapPool.get(width, height, options.inPreferredConfig);
                //inbitmap 不为null时，则不创建新的bitmap用该值复用

                options.inBitmap = bitmap;
                options.inMutable = true;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = Tool.sampleBitmapSize(options, width, height);
                Bitmap resoult = BitmapFactory.decodeStream(inputStream);
                imgResource.setBitmap(resoult);
                imgResource.setKey(key);

                return imgResource;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if (editor != null) {
                try {
                    editor.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        return null;
    }
}
