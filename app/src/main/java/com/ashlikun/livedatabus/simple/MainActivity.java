package com.ashlikun.livedatabus.simple;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ashlikun.livedatabus.EventBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.get("key1")
                .registerForever(new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        Log.e("MainActivity", "接受到数据" + s);
                    }
                });
    }

    public void onClick(View view) {
        EventBus.get("key2")
                .post("33333");
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
