package com.ashlikun.livedatabus

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/10　15:44
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：消息通道,就是一个key对应一个通道
 * 泛型代表观察的对象
 * 封装MutableLiveData
 */


public class XLiveData<T> {
    private var busLiveData = MutableLiveData<T>()

    /**
     * 方法功能：从context中获取activity，如果context不是activity那么久返回null
     */
    fun getActivity(context: Context?): Activity? {
        if (context == null) return null
        if (context is Activity) return context
        else if (context is ContextWrapper) return getActivity(context.baseContext)
        return null
    }

    /**
     * 发送一个事件
     * 这里判断线程，如果不是主线程会切换到主线程
     */
    @JvmOverloads
    fun post(value: T? = null) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            busLiveData.postValue(value)
        } else {
            busLiveData.value = value
        }
    }

    /**
     * 注册订阅
     * 这种注册只会在activity生命周期内执行(获取焦点)，如果发送的时候没有获取焦点，那么会在获取焦点的时候触发
     *
     * X:不会接收之前的消息
     * 不需要手动取消订阅
     */
    fun observeX(owner: LifecycleOwner, observer: Observer<out T?>) {
        busLiveData.observe(owner, observer as Observer<Any?>)
        try {
            BusUtils.hook(busLiveData, observer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 注册订阅
     * 这种注册只会在activity生命周期内执行(获取焦点)，如果发送的时候没有获取焦点，那么会在获取焦点的时候触发
     * X:不会接收之前的消息
     * 不需要手动取消订阅
     */
    fun observeX2(context: Any, observer: Observer<out T?>) {
        if (context is LifecycleOwner) {
            observeX(context, observer)
        } else if (context is Context) {
            val activity = getActivity(context)
            if (activity != null && activity is LifecycleOwner) {
                observeX(activity as LifecycleOwner, observer)
            }
        }
    }

    /**
     * 注册订阅
     * Sticky:这样订阅者可以接收到订阅之前发送的消息
     * 不需要手动取消订阅
     */
    fun observe(owner: LifecycleOwner, observer: Observer<out T?>) {
        busLiveData.observe(owner, observer as Observer<Any?>)
    }

    /**
     * 注册订阅
     * Sticky:这样订阅者可以接收到订阅之前发送的消息
     * 不需要手动取消订阅
     */
    fun observe2(context: Any, observer: Observer<out T?>) {
        if (context is LifecycleOwner) {
            observe(context, observer)
        } else if (context is Context) {
            val activity = getActivity(context)
            if (activity != null && activity is LifecycleOwner) {
                observe(activity as LifecycleOwner, observer)
            }
        }
    }

    /**
     * 永久注册
     *
     * 需要手动取消订阅
     */
    fun observeForeverX(observer: Observer<out T?>) {
        busLiveData.observeForever(ObserverWrapper(observer as Observer<Any?>))
    }

    /**
     * 永久注册
     * 需要手动取消订阅，Sticky模式
     *
     * @param observer
     */
    fun observeForever(observer: Observer<out T?>) {
        busLiveData.observeForever(observer as Observer<Any?>)
    }

    /**
     * 取消订阅
     * Forever模式的都要主动取消
     */
    fun unObserve(observer: Observer<out T?>) {
        busLiveData.removeObserver(observer as Observer<Any?>)
    }
}