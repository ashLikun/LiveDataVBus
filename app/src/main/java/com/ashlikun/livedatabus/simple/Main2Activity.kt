package com.ashlikun.livedatabus.simple

import android.os.Bundle
import com.ashlikun.livedatabus.simple.R
import com.ashlikun.livedatabus.EventBus
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ashlikun.livedatabus.busPost
import com.ashlikun.livedatabus.busSticky
import kotlinx.coroutines.*

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/22 0022　下午 3:06
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class Main2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        busSticky("key2", {
            Log.e("Main2Activity", "接受到Sticky数据aa$it")
        })
        "key2".busSticky(this) {
            Log.e("Main2Activity", "接受到Sticky2数据aa$it")
        }
        "key444".busPost("dasdasd")
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(SupervisorJob()).launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                EventBus["keyaaa"].observeX(this@Main2Activity) {
                    Log.e("Main2Activity", "aaaaaaaaaaaaaa")
                }
            }
        }

    }

    fun onClick(view: View?) {
        "key1".busPost()
        "keyaaa".busPost()
    }
}