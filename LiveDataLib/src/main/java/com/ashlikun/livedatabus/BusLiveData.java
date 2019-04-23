package com.ashlikun.livedatabus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/10　15:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：观察的数据核心,不会对外
 */
class BusLiveData extends MutableLiveData<Object> {
    /**
     * 自定义永久的观察者合集
     */
    private Map<Observer, Observer> observerMap = new HashMap<>();

    /**
     * 移除永久订阅
     *
     * @param observer
     */
    @Override
    public void removeObserver(@NonNull Observer observer) {
        Observer realObserver = null;
        if (observerMap.containsKey(observer)) {
            realObserver = observerMap.remove(observer);
        } else {
            realObserver = observer;
        }
        super.removeObserver(realObserver);
    }

    public Observer putForever(@NonNull Observer observer) {
        if (!observerMap.containsKey(observer)) {
            observerMap.put(observer, new ObserverWrapper(observer));
        }
        return observerMap.get(observer);
    }

    protected void hook(@NonNull Observer observer) throws Exception {
        //get wrapper's version
        Class<LiveData> classLiveData = LiveData.class;
        Field fieldObservers = classLiveData.getDeclaredField("mObservers");
        fieldObservers.setAccessible(true);
        Object objectObservers = fieldObservers.get(this);
        Class<?> classObservers = objectObservers.getClass();
        Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
        methodGet.setAccessible(true);
        Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
        Object objectWrapper = null;
        if (objectWrapperEntry instanceof Map.Entry) {
            objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
        }
        if (objectWrapper == null) {
            throw new NullPointerException("Wrapper can not be bull!");
        }
        Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
        Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
        fieldLastVersion.setAccessible(true);
        //get livedata's version
        Field fieldVersion = classLiveData.getDeclaredField("mVersion");
        fieldVersion.setAccessible(true);
        Object objectVersion = fieldVersion.get(this);
        //set wrapper's version
        fieldLastVersion.set(objectWrapper, objectVersion);
    }
}
