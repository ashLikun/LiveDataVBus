package com.ashlikun.livedatabus

import androidx.lifecycle.*

/**
 * 作者　　: 李坤
 * 创建时间: 2022/3/10　23:43
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
internal object BusUtils {
    /**
     * 解决可以接收到订阅之前发送的消息
     */
    @Throws(Exception::class)
    fun hook(liveData: LiveData<*>, observer: Observer<*>) {
        //get wrapper's version
        val classLiveData = LiveData::class.java
        val fieldObservers = classLiveData.getDeclaredField("mObservers")
        fieldObservers.isAccessible = true
        val objectObservers = fieldObservers[liveData]
        val classObservers: Class<*> = objectObservers.javaClass
        val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
        methodGet.isAccessible = true
        val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
        var objectWrapper: Any? = null
        if (objectWrapperEntry is Map.Entry<*, *>) {
            objectWrapper = objectWrapperEntry.value
        }
        if (objectWrapper == null) {
            throw NullPointerException("Wrapper can not be bull!")
        }
        val classObserverWrapper: Class<*>? = objectWrapper.javaClass.superclass
        val fieldLastVersion = classObserverWrapper!!.getDeclaredField("mLastVersion")
        fieldLastVersion.isAccessible = true
        //get livedata's version
        val fieldVersion = classLiveData.getDeclaredField("mVersion")
        fieldVersion.isAccessible = true
        val objectVersion = fieldVersion[liveData]
        //set wrapper's version
        fieldLastVersion[objectWrapper] = objectVersion
    }


}

/**
 * 只有在OnResumed才接收消息
 */
fun LifecycleOwner.liveDataResumed(): LifecycleOwner {
    //返回一个代理
    return LifecycleOwner {
        object : Lifecycle() {
            override fun addObserver(observer: LifecycleObserver) {
                lifecycle.addObserver(observer)
            }

            override fun removeObserver(observer: LifecycleObserver) {
                lifecycle.removeObserver(observer)
            }

            override fun getCurrentState(): State {
                //如果是start就返回在create，这样LiveData内部就不能判断是start
                return when {
                    lifecycle.currentState == State.STARTED -> {
                        State.CREATED
                    }
                    else -> {
                        lifecycle.currentState
                    }
                }
            }

        }
    }
}


/**
 * 加入计数,达到次数就回调
 */
fun <T> Observer<T>.count(count: Int = 0): Observer<T> {
    var count = count
    var current = 0
    return Observer<T> {
        current++
        if (current >= count) {
            current = 0
            this.onChanged(it)
        }
    }
}
