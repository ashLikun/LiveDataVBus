package com.ashlikun.livedatabus

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlin.jvm.Synchronized

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/10 15:32
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：事件总线
 */
//注册事件扩展方法
/**
 * 注册订阅
 * 这种注册只会在activity生命周期内执行(获取焦点)，如果发送的时候没有获取焦点，那么会在获取焦点的时候触发
 *
 * 不需要手动取消订阅
 */
inline fun LifecycleOwner.bus(key: String, observer: Observer<out Any?>) =
    EventBus[key].observeX(this, observer)

inline fun Context.busContext(key: String, observer: Observer<out Any?>) =
    EventBus[key].observeX2(this, observer)

inline fun String.bus(lifecycleOwner: LifecycleOwner, observer: Observer<out Any?>) =
    EventBus[this].observeX(lifecycleOwner, observer)

/**
 * 注册订阅
 * Sticky:这样订阅者可以接收到订阅之前发送的消息
 * 不需要手动取消订阅
 */
inline fun LifecycleOwner.busSticky(key: String, observer: Observer<out Any?>) =
    EventBus[key].observe(this, observer)

inline fun Context.busContextSticky(key: String, observer: Observer<out Any?>) =
    EventBus[key].observe2(this, observer)

inline fun String.busSticky(lifecycleOwner: LifecycleOwner, observer: Observer<out Any?>) =
    EventBus[this].observe(lifecycleOwner, observer)

/**
 * 永久注册
 *
 * 需要手动取消订阅
 */

inline fun String.busForever(observer: Observer<Any?>) =
    EventBus[this].observeForeverX(observer)

inline fun String.busStickyForever(observer: Observer<Any?>) =
    EventBus[this].observeForever(observer)

/**
 * 反注册
 */
inline fun Observer<Any?>.unBus(key: String) =
    EventBus[key].unObserve(this)

inline fun String.unBus(observer: Observer<Any?>) =
    EventBus[this].unObserve(observer)

/**
 * 发送事件扩展方法
 */
inline fun String.busPost(value: Any? = null) =
    EventBus[this].post(value)

class EventBus private constructor() {
    /**
     * 存放消息通道的map
     */
    private val bus: MutableMap<String, XLiveData<Any?>> = mutableMapOf()

    /**
     * 获得这个key对应的消息通道
     *
     * @param key
     * @return
     */
    @Synchronized
    private fun with(key: String): XLiveData<Any?> {
        if (!bus.containsKey(key)) {
            bus[key] = XLiveData()
        }
        return bus[key]!!
    }

    companion object {
        private val instance by lazy { EventBus() }
        fun get(): EventBus = instance

        /**
         * 获得这个key对应的消息通道
         */
        operator fun get(key: String) = get().with(key)
    }


}