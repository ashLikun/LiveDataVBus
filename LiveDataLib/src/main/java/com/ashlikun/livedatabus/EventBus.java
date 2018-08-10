package com.ashlikun.livedatabus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/10 15:32
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：事件总线
 */

public final class EventBus {
    private static volatile EventBus instance = null;

    /**
     * 单例模式
     *
     * @return
     */
    public static EventBus get() {
        //双重校验DCL单例模式
        if (instance == null) {
            //同步代码块
            synchronized (EventBus.class) {
                if (instance == null) {
                    //创建一个新的实例
                    instance = new EventBus();
                }
            }
        }
        //返回一个实例
        return instance;
    }

    /**
     * 获得这个key对应的消息通道
     *
     * @param key
     * @return
     */
    public static BusChannel get(String key) {
        get();
        return instance.with(key);
    }

    /**
     * 存放消息通道的map
     */
    private final Map<String, BusChannel> bus;

    private EventBus() {
        bus = new HashMap<>();
    }

    /**
     * 获得这个key对应的消息通道
     *
     * @param key
     * @return
     */
    private synchronized BusChannel with(String key) {
        if (!bus.containsKey(key)) {
            bus.put(key, new BusChannel());
        }
        return bus.get(key);
    }

}
