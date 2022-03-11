package com.ashlikun.livedatabus

import java.lang.StackTraceElement
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/10　15:45
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
internal class ObserverWrapper<T>(observer: Observer<T>) : Observer<T> {
    private val observer: Observer<T>?
    override fun onChanged(t: T) {
        if (observer != null) {
            if (isCallOnObserve) {
                return
            }
            observer.onChanged(t)
        }
    }

    private val isCallOnObserve: Boolean
        private get() {
            val stackTrace = Thread.currentThread().stackTrace
            if (stackTrace != null && stackTrace.size > 0) {
                for (element in stackTrace) {
                    if (LiveData::class.java.name == element.className && "observeForever" == element.methodName) {
                        return true
                    }
                }
            }
            return false
        }

    init {
        this.observer = observer
    }
}