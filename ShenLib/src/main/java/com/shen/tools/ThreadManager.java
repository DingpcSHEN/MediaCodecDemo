package com.shen.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
    private static ThreadManager manager = new ThreadManager();

    //单线程池
    private ExecutorService singlePool;
    /**
     * 可缓存线程池 生命周期很短暂并且需要立即响应
     * new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
     * 核心线程数为0 非核心线程数无穷大 任务队列是异步队列
     * 因此提交一个任务后会立即开启一个非核心线程执行
     */
    private ExecutorService cachedPool;
    /**
     * 固定数量线程 生命周期比较稳定或者比较长的任务
     * new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
     * 核心线程数和非核心线程数都为指定数量 任务队列是无界等待队列
     * 因此指定的任务数量最好超过整个应用固定线程数量
     * 除此之外需要注意一个问题 因为采用的是无界队列 所以非核心线程永远不会启动 实际上就只有指定数量的核心线程运行而已
     */
    private ExecutorService fixedPool;


    public static ThreadManager getInstance(){
        return manager;
    }

    private ThreadManager(){
        singlePool=Executors.newSingleThreadExecutor();
        cachedPool=Executors.newCachedThreadPool();
        fixedPool=Executors.newFixedThreadPool(15);
    }

    /**
     * 运行实时线程
     * @param runnable
     */
    public void executeRealTimeRunnable(Runnable runnable){
        cachedPool.execute(runnable);
    }

    public void executeFixedRunnable(Runnable runnable){
        fixedPool.execute(runnable);
    }
    /**
     * 关闭线程池
     */
    public void shutdown(){
        try{
            if (singlePool != null) singlePool.shutdown();
        }catch (Exception e){
        }
        try{
            if (cachedPool != null) cachedPool.shutdown();
        }catch (Exception e){
        }
        try{
            if (fixedPool != null) fixedPool.shutdown();
        }catch (Exception e){
        }
    }




    /**
     * 睡眠
     * @param millise
     */
    public static void sleep(int millise){
        try{
            Thread.sleep(millise);
        }catch (Exception e){
        }
    }
}
