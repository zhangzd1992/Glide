package com.example.zhangzd.glide_demo.glide.resource;

import com.example.zhangzd.glide_demo.glide.util.Tool;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-27 13:53
 */
public class Key {
    private String key;
    public Key(String path){

        this.key = Tool.getSHA256StrJava(path);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
