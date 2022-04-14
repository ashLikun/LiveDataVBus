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

    /**
     * 只有在OnResme才接收消息
     */
    @Throws(Exception::class)
    fun hookCurrentState(lifecycleOwner: LifecycleOwner, observer: Observer<*>): LifecycleOwner {

        return LifecycleOwner {
            object : Lifecycle() {
                override fun addObserver(observer: LifecycleObserver) {
                    lifecycleOwner.lifecycle.addObserver(observer)
                }

                override fun removeObserver(observer: LifecycleObserver) {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }

                override fun getCurrentState(): State {
                    if (lifecycleOwner.lifecycle.currentState.isAtLeast(State.STARTED)) {
                        return State.RESUMED
                    } else {
                        return State.RESUMED
                    }
                }

            }
        }
    }
}