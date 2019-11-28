package com.example.zhangzd.glide_demo.glide;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-28 13:39
 */
public interface LifecycleCallback {
    // 生命周期初始化了
    void glideInitAction();

    // 生命周期 停止了
    void glideStopAction();

    // 生命周期 释放 操作了
    void glideRecycleAction();
}
