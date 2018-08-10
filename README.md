
[![Release](https://jitpack.io/v/ashLikun/LiveDataBus.svg)](https://jitpack.io/#ashLikun/LiveDataBus)

# **LiveDataBus**
Android消息总线，基于LiveData，具有生命周期感知能力，支持Sticky

build.gradle文件中添加:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
并且:

```gradle
dependencies {
    implementation 'com.github.ashLikun:LiveDataBus:{latest version}'//LiveDataBus
}
### 1.用法
```java
         //注册
         EventBus.get("key1")
                        .registerForever(new Observer<String>() {
                            @Override
                            public void onChanged(@Nullable String s) {
                                Log.e("MainActivity", "接受到数据" + s);
                            }
                        });

         //发送事件，不用担心线程问题
         EventBus.get("key2")
                  .post("33333");



        /**
         * 注册订阅
         * 这种注册只会在activity生命周期内执行(获取焦点)，如果发送的时候没有获取焦点，那么会在获取焦点的时候出发
         * <p>
         * 不需要手动取消订阅
         */
        registerLifecycle




         /**
         * 注册订阅
         * Sticky:这样订阅者可以接收到订阅之前发送的消息
         * 不需要手动取消订阅
         */
         registerSticky


         /**
          * 永久注册
          * <p>
          * 需要手动取消订阅
          *
          * @param observer
          */
          registerForever

        /**
           * 永久注册
           * 需要手动取消订阅，Sticky模式
           *
           * @param observer
           */
          registerStickyForever
```
### 混肴
####


