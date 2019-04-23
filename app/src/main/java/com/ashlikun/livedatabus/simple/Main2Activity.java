package com.ashlikun.livedatabus.simple;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ashlikun.livedatabus.EventBus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/22 0022　下午 3:06
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        EventBus.get("key2")
                .registerSticky(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        Log.e("Main2Activity", "接受到数据" + s);
                    }
                });
    }

    public void onClick(View view) {
        EventBus.get("key1")
                .post("2222222");
    }

}
