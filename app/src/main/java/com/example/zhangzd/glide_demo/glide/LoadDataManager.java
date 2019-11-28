package com.example.zhangzd.glide_demo.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.zhangzd.glide_demo.glide.resource.ImgResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-28 15:00
 */
public class LoadDataManager implements Runnable ,ILoadData{
    private final String TAG = LoadDataManager.class.getSimpleName();

    private String path;
    private ResponseListener responseListener;
    private Context context;

    @Override
    public ImgResource loadResource(String path, ResponseListener responseListener, Context context) {
        this.path = path;
        this.responseListener = responseListener;
        this.context = context;

        // 加载 网络图片/SD本地图片/..

        Uri uri = Uri.parse(path);

        // 网络图片
        if ("HTTP".equalsIgnoreCase(uri.getScheme()) || "HTTPS".equalsIgnoreCase(uri.getScheme())) {
            new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                    60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>()).execute(this);
        }

        // SD本地图片 返回Value

        // ....

        return null;
    }


    @Override
    public void run() {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(path);
            URLConnection urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setConnectTimeout(5000);
            final int responseCode = httpURLConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                inputStream = httpURLConnection.getInputStream();





                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // 成功 切换主线程
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ImgResource value = ImgResource.getInstance();
                        value.setBitmap(bitmap);

                        // 回调成功
                        responseListener.responseSuccess(value);
                    }
                });
            } else {
                // 失败 切换主线程
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        responseListener.responseException(new IllegalStateException("请求失败 请求码:" + responseCode));
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: 关闭 inputStream.close(); e:" + e.getMessage());
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}
