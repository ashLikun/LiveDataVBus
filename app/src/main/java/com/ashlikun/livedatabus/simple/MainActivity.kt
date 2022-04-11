package com.ashlikun.livedatabus.simple

import android.os.Bundle
import com.ashlikun.livedatabus.EventBus
import android.util.Log
import android.view.View
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.ashlikun.livedatabus.bus

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus["key2"]
            .post("33333")
        EventBus["key2"]
            .post("44444")
        EventBus["key2"]
            .observeForeverX { s -> Log.e("MainActivity", "接受到数据$s") }

        EventBus["key1"]
            .observeForeverX { s -> Log.e("MainActivity", "1接受到数据$s") }

        bus("key1") {
            Log.e("MainActivity", "3接受到数据bus  $it")
        }
    }

    fun onClick(view: View?) {
        val intent = Intent(this, Main2Activity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}