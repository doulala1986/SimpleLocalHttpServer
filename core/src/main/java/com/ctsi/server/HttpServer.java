package com.ctsi.server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by doulala on 2016/10/26.
 */

public class HttpServer {
    private static final String TAG = "HttpServer";

    int port;

    ExecutorService pool;

    boolean isEnable = true;

    ServerSocket socket;

    public HttpServer(int port) {
        this.port = port;

        pool = (ExecutorService) Executors.newCachedThreadPool();
    }


    public void startAync() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                listen();
            }
        }).start();


    }

    private void listen() {

        this.isEnable = true;
        InetSocketAddress address = new InetSocketAddress(port);
        try {
            socket = new ServerSocket();
            socket.bind(address);
            while (this.isEnable) {
                final Socket socket_require = socket.accept();

                pool.submit(new Runnable() {
                    @Override
                    public void run() {

                        Log.e(TAG, "requirer address:" + socket_require.getInetAddress().toString());

                        handleMySocket(socket_require);

                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void stopAync() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                close();
            }
        });


    }

    private void close() {
        if (!isEnable) {
            return;
        } else {
            this.isEnable = false;
            try {
                this.socket.close();
                this.socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleMySocket(Socket socket_require) {

        Log.e("catch", "handleMySocket");
        try {
            InputStream is = socket_require.getInputStream();
            String body = StreamUtil.convertStreamToString(is);
            Log.e("body", body);
            socket_require.getOutputStream().write(body.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        socket_require.close();

    }


}
