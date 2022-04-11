package com.ashlikun.livedatabus

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * 作者　　: 李坤
 * 创建时间: 2022/4/11　22:33
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */

/**
 * 加入计数,达到次数就回调
 */
fun <T> observerCount(count: Int = 0, observer: Observer<T>): Observer<T> {
    var count = count
    var current = 0
    return Observer<T> {
        current++
        if (current >= count) {
            observer.onChanged(it)
        }
    }
}
