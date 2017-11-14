package com.bdlog.demo.utils;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 发送url数据的监控者，用于启动一个单独的线程来发送数据
 */
public class SendDataMonitor {
    //队列，用于存储发送url
    private BlockingDeque<String> blockingDeque = new LinkedBlockingDeque<>();
    //私有构造方法，进行单例模式的创建
    private SendDataMonitor(){}
    public static void addSendUrl(String url) {
        try {
            getInstance().blockingDeque.put(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static SendDataMonitor mSendDataMonitor = null;
    public static SendDataMonitor getInstance(){
        if(mSendDataMonitor==null){
            synchronized (SendDataMonitor.class){
                if(mSendDataMonitor==null){
                    mSendDataMonitor = new SendDataMonitor();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //线程中调用具体的处理方法
                            SendDataMonitor.mSendDataMonitor.run();
                        }
                    });
                    thread.start();
                }
            }
        }
        return mSendDataMonitor;
    }

    private void run(){
        while (true){
            try {
                String url = this.blockingDeque.take();
                HttpRequestUtil.sendData(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
