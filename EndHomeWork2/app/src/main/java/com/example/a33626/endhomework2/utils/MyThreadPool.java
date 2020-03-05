package com.example.a33626.endhomework2.utils;

import com.example.a33626.endhomework2.constants.Constants;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 这个就是我封装好的单例线程池
 *
 */
public class MyThreadPool{

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            //这些是线程池初始化的参数  用的是自定义的线程池
            Constants.DEFAULT_CORE_SIZE,
            Constants.MAX_QUEUE_SIZE,
            Integer.MAX_VALUE,
            TimeUnit.MILLISECONDS,
            //无界线性队列
            new LinkedBlockingDeque<Runnable>()
    );
    //volatile 关键字保证了对象对象 的可见性 和 线程的安全性
    private volatile static MyThreadPool instance = null;

    private MyThreadPool(){}


    /**
     * 获取单例线程池
     */
    //单例
    public static MyThreadPool getInstance(){
        if (instance == null){
            synchronized (MyThreadPool.class){
                if (instance == null){
                    instance = new MyThreadPool();
                }
            }
        }
        return instance;
    }


    /**
     * 执行任务
     */

    public void execute(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        executor.execute(runnable);
    }

    /**
     * 关闭线程池
     */

    public void shutdown(){
        executor.shutdown();
    }

    public void getActiveCount(){
        System.out.println(executor.getActiveCount());
    }
}
