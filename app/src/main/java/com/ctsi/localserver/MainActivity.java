package com.ctsi.localserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ctsi.server.HttpServer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpServer server=new HttpServer(8082);
        server.startAync();
    }
}
