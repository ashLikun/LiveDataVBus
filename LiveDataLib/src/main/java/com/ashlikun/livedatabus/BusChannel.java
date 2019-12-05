package com.ashlikun.livedatabus;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/10　15:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：消息通道,就是一个key对应一个通道
 * 泛型代表观察的对象
 */
public class BusChannel {
    BusLiveData busLiveData = new BusLiveData();

    /**
     * 方法功能：从context中获取activity，如果context不是activity那么久返回null
     */
    private Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 主线程handle
     */
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 发送一个事件
     * 这里判断线程，如果不是主线程会切换到主线程
     *
     * @param value
     */
    public void post(Object value) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            mainHandler.post(new PostValueTask(value, busLiveData));
        } else {
            busLiveData.setValue(value);
        }
    }

    /**
     * 发送一个事件
     * 默认数据为null
     */
    public void post() {
        post(null);
    }

    /**
     * 注册订阅
     * 这种注册只会在activity生命周期内执行(获取焦点)，如果发送的时候没有获取焦点，那么会在获取焦点的时候触发
     * <p>
     * 不需要手动取消订阅
     */
    public void registerLifecycle(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        busLiveData.observe(owner, observer);
        try {
            busLiveData.hook(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册订阅
     * 这种注册只会在activity生命周期内执行(获取焦点)，如果发送的时候没有获取焦点，那么会在获取焦点的时候触发
     * <p>
     * 不需要手动取消订阅
     */
    public void registerLifecycle2(@NonNull Object context, @NonNull Observer observer) {
        if (context instanceof LifecycleOwner) {
            registerLifecycle((LifecycleOwner) context, observer);
        } else if (context instanceof Context) {
            Activity activity = getActivity((Context)context);
            if (activity != null && activity instanceof LifecycleOwner) {
                registerLifecycle((LifecycleOwner) activity, observer);
            }
        }
    }

    /**
     * 注册订阅
     * Sticky:这样订阅者可以接收到订阅之前发送的消息
     * 不需要手动取消订阅
     */
    public void registerSticky(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        busLiveData.observe(owner, observer);
    }

    /**
     * 注册订阅
     * Sticky:这样订阅者可以接收到订阅之前发送的消息
     * 不需要手动取消订阅
     *
     * @param context
     */
    public void registerSticky2(@NonNull Object context, @NonNull Observer observer) {
        if (context instanceof LifecycleOwner) {
            registerSticky((LifecycleOwner) context, observer);
        } else if (context instanceof Context) {
            Activity activity = getActivity((Context) context);
            if (activity != null && activity instanceof LifecycleOwner) {
                registerSticky((LifecycleOwner) activity, observer);
            }
        }
    }

    /**
     * 永久注册
     * <p>
     * 需要手动取消订阅
     *
     * @param observer
     */
    public void registerForever(@NonNull Observer observer) {
        busLiveData.observeForever(busLiveData.putForever(observer));
    }

    /**
     * 永久注册
     * 需要手动取消订阅，Sticky模式
     *
     * @param observer
     */
    public void registerStickyForever(@NonNull Observer observer) {
        busLiveData.observeForever(observer);
    }

    /**
     * 取消订阅
     * Forever模式的都要主动取消
     *
     * @param observer
     */
    public void unRegister(@NonNull Observer observer) {
        busLiveData.removeObserver(observer);
    }
}
