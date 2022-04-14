package com.ashlikun.livedatabus.simple

import android.os.Bundle
import android.util.Log
import android.view.View
import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ashlikun.livedatabus.*

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

        "key444".bus(this.liveDataResumed(), Observer<String> {
            Log.e("MainActivity", "key444接受到数据bus  $it  ${lifecycle.currentState}")
        })
    }

    fun onClick(view: View?) {
        val intent = Intent(this, Main2Activity::class.java)
        startActivity(intent)
    }

    fun onClickDialog(view: View?) {
        AlertDialog.Builder(this)
            .setTitle("测试OnResume才能接收消息")
            .setView(LayoutInflater.from(this).inflate(R.layout.dialog, null).apply {
                findViewById<View>(R.id.ceshi).setOnClickListener {
                    "key444".busPost("aaaaa")
                }
            })
            .show()
    }

    override fun onPause() {
        super.onPause()
        Log.e("MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}