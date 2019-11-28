package com.example.zhangzd.glide_demo.glide.cache;

import android.util.ArrayMap;

import com.example.zhangzd.glide_demo.glide.resource.ImageNoUseCallback;
import com.example.zhangzd.glide_demo.glide.resource.ImgResource;
import com.example.zhangzd.glide_demo.glide.util.Tool;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description:  活动缓存，用于缓存正在加载的图片资源
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 13:45
 */
public class ActiveCache {

    private Map<String,CusWeakReference> cacheMap = new ArrayMap<>();
    private ReferenceQueue<ImgResource> queue;
    private boolean isShouDongRemove;   // 是否是手动移除
    private boolean isCloseThread; //标记线程是否关闭
    private Thread thread;
    private ImageNoUseCallback callback;


    public ActiveCache(ImageNoUseCallback callback){
        this.callback = callback;
    }


    public void put(String key,ImgResource resource){
        if (callback != null) {
            resource.setNoUseCallback(callback);
        }
        cacheMap.put(key,new CusWeakReference(resource, getQueue(), key));
    }


    public ImgResource get(String key){
        CusWeakReference cusWeakReference = cacheMap.get(key);
        if (cusWeakReference != null) {
            return cusWeakReference.get();
        }
        return null;
    }


    public ImgResource remove(String key) {
        Tool.checkNotEmpty(key);
        isShouDongRemove = true;
        CusWeakReference remove = cacheMap.remove(key);
        isShouDongRemove = false;
        if (remove != null) {

            return remove.get();
        }

        return null;
    }


    private ReferenceQueue<ImgResource> getQueue() {
        if (queue ==  null) {
            queue = new ReferenceQueue<>();

            //开启线程，监听gc兹自动移除
            thread =  new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (!isCloseThread) {
                        try {
                            CusWeakReference remove = (CusWeakReference) queue.remove();
                            if (cacheMap != null && cacheMap.isEmpty() && !isShouDongRemove) {
                                cacheMap.remove(remove.key);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }
        return queue;
    }


    public void closeThread() {
        isCloseThread = true;
        if (null != thread) {
            thread.interrupt();
            try {
                thread.join(TimeUnit.SECONDS.toMillis(5)); // 线程稳定安全 停止下来
                if (thread.isAlive()) { // 证明线程还是没有结束
                    throw new IllegalStateException("活动缓存中 关闭线程 线程没有停止下来...");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public class CusWeakReference extends WeakReference<ImgResource> {
        private  String key;
        public CusWeakReference(ImgResource referent, ReferenceQueue<? super ImgResource> q, String key) {
            super(referent, q);
            this.key = key;
        }
    }
}
