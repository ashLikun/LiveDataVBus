package com.ashlikun.livedatabus;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/10　15:42
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：执行一个事件的Runnable
 */
class PostValueTask implements Runnable {
    private Object newValue;
    MutableLiveData liveData;

    public PostValueTask(@NonNull Object newValue, MutableLiveData liveData) {
        this.newValue = newValue;
        this.liveData = liveData;
    }

    @Override
    public void run() {
        liveData.setValue(newValue);
    }
}