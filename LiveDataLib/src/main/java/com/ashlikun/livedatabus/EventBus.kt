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
inline fun LifecycleOwner.bus(key: String, observer: Observer<out Any>) =
    EventBus[key].observeX(this, observer)

inline fun LifecycleOwner.busResume(key: String, observer: Observer<out Any>) =
    EventBus[key].observeXResume(this, observer)

inline fun LifecycleOwner.busCreate(key: String, observer: Observer<out Any>) =
    EventBus[key].observeXCreate(this, observer)

inline fun Context.busContext(key: String, observer: Observer<out Any>) =
    EventBus[key].observeX2(this, observer)

inline fun String.bus(lifecycleOwner: LifecycleOwner, observer: Observer<out Any>) =
    EventBus[this].observeX(lifecycleOwner, observer)

inline fun String.busResume(lifecycleOwner: LifecycleOwner, observer: Observer<out Any>) =
    EventBus[this].observeXResume(lifecycleOwner, observer)

inline fun String.busCreate(lifecycleOwner: LifecycleOwner, observer: Observer<out Any>) =
    EventBus[this].observeXCreate(lifecycleOwner, observer)

/**
 * 注册订阅
 * Sticky:这样订阅者可以接收到订阅之前发送的消息
 * 不需要手动取消订阅
 */
inline fun LifecycleOwner.busSticky(key: String, observer: Observer<out Any>) =
    EventBus[key].observe(this, observer as Observer<Any>)

inline fun LifecycleOwner.busStickyResume(key: String, observer: Observer<out Any>) =
    EventBus[key].observeResume(this, observer as Observer<Any>)

inline fun LifecycleOwner.busStickyCreate(key: String, observer: Observer<out Any>) =
    EventBus[key].observeCreate(this, observer as Observer<Any>)

inline fun Context.busContextSticky(key: String, observer: Observer<out Any>) =
    EventBus[key].observe2(this, observer as Observer<Any>)

inline fun String.busSticky(lifecycleOwner: LifecycleOwner, observer: Observer<out Any>) =
    EventBus[this].observe(lifecycleOwner, observer as Observer<Any>)

inline fun String.busStickyResume(lifecycleOwner: LifecycleOwner, observer: Observer<out Any>) =
    EventBus[this].observeResume(lifecycleOwner, observer as Observer<Any>)

inline fun String.busStickyCreate(lifecycleOwner: LifecycleOwner, observer: Observer<out Any>) =
    EventBus[this].observeCreate(lifecycleOwner, observer as Observer<Any>)

inline fun String.busUnObserve(observer: Observer<out Any>) =
    EventBus[this].unObserve(observer as Observer<Any>)

/**
 * 永久注册
 *
 * 需要手动取消订阅
 */

inline fun String.busForever(observer: Observer<out Any>) =
    EventBus[this].observeForeverX(observer)

inline fun String.busStickyForever(observer: Observer<out Any>) =
    EventBus[this].observeForever(observer as Observer<Any>)

/**
 * 反注册
 */
inline fun Observer<out Any>.unBus(key: String) =
    EventBus[key].unObserve(this)

inline fun String.unBus(observer: Observer<out Any>) =
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
    private val bus: MutableMap<String, XLiveData<Any>> = mutableMapOf()

    /**
     * 获得这个key对应的消息通道
     *
     * @param key
     * @return
     */
    @Synchronized
    private fun with(key: String): XLiveData<Any> {
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