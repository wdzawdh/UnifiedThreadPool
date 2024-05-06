package com.cw.asm.unifiedthread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 统一线程池
 * <p>
 * Hook Executors
 * Executors.newFixedThreadPool -> ThreadUtil.newFixedThreadPool
 */
public class ThreadUtil {

    private static final int coreCount = Runtime.getRuntime().availableProcessors();

    private final static String THREAD_NAME_STEM = "opt_thread_%d";

    private final static String FIXED_NAME_STEM = "opt_fixed_%d";

    private final static String SINGLE_NAME_STEM = "opt_single";

    private final static String CACHED_NAME_STEM = "opt_cached_%d";

    private final static String SCHEDULED_NAME_STEM = "opt_scheduled_%d";

    private static ThreadPoolExecutor threadPool;

    private static ThreadPoolExecutor fixedThreadPool;

    private static ThreadPoolExecutor cachedThreadPool;

    private static ScheduledThreadPoolExecutor scheduledThreadPool;

    /**
     * java/util/concurrent/Executors
     * newSingleThreadExecutor
     * (I)Ljava/util/concurrent/ExecutorService;
     */
    public static ExecutorService newSingleThreadExecutor() {
        return getSingleThreadPool();
    }

    /**
     * java/util/concurrent/Executors
     * newSingleThreadExecutor
     * (Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;
     */
    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return getSingleThreadPool();
    }

    /**
     * java/util/concurrent/Executors
     * newFixedThreadPool
     * (I)Ljava/util/concurrent/ExecutorService;
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        if (nThreads == 1) {
            return getSingleThreadPool();
        }
        return getFixedThreadPool();
    }

    /**
     * java/util/concurrent/Executors
     * newFixedThreadPool
     * (ILjava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;
     */
    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        if (nThreads == 1) {
            return getSingleThreadPool();
        }
        return getFixedThreadPool();
    }

    /**
     * java/util/concurrent/Executors
     * newCachedThreadPool
     * ()Ljava/util/concurrent/ExecutorService;
     */
    public static ExecutorService newCachedThreadPool() {
        return getCachedThreadPool();
    }

    /**
     * java/util/concurrent/Executors
     * newCachedThreadPool
     * (Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;
     */
    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return getCachedThreadPool();
    }

    /**
     * java/util/concurrent/Executors
     * newScheduledThreadPool
     * (I)Ljava/util/concurrent/ScheduledExecutorService;
     */
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return getScheduledThreadPool();
    }

    /**
     * java/util/concurrent/Executors
     * newScheduledThreadPool
     * (ILjava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ScheduledExecutorService;
     */
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        return getScheduledThreadPool();
    }

    /**
     * java/util/concurrent/Executors
     * newSingleThreadScheduledExecutor
     * ()Ljava/util/concurrent/ScheduledExecutorService;
     */
    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return getScheduledThreadPool();
    }

    /**
     * java/util/concurrent/Executors
     * newSingleThreadScheduledExecutor
     * (Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ScheduledExecutorService;
     */
    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return getScheduledThreadPool();
    }

    public static synchronized ThreadPoolExecutor getThreadPool() {
        if (!isEnableExecutor(threadPool)) {
            threadPool = new ThreadPoolExecutor(
                    coreCount, // 核心线程数
                    64, // 最大线程数
                    30000, // 线程存活时间
                    TimeUnit.MILLISECONDS, // 线程存活时间单位
                    new LinkedBlockingQueue<>(), // 任务队列
                    getThreadFactory(THREAD_NAME_STEM),
                    (r, executor) -> getThreadPool().execute(r)
            );
        }
        return threadPool;
    }

    public static synchronized ThreadPoolExecutor getFixedThreadPool() {
        if (!isEnableExecutor(fixedThreadPool)) {
            fixedThreadPool = new ThreadPoolExecutor(
                    coreCount, // 核心线程数
                    coreCount, // 最大线程数
                    0, // 线程存活时间
                    TimeUnit.MILLISECONDS, // 线程存活时间单位
                    new LinkedBlockingQueue<>(), // 任务队列
                    getThreadFactory(FIXED_NAME_STEM),
                    (r, executor) -> getFixedThreadPool().execute(r)
            );
        }
        return fixedThreadPool;
    }

    public static synchronized ThreadPoolExecutor getSingleThreadPool() {
        return new ThreadPoolExecutor(
                1, // 核心线程数
                1, // 最大线程数
                0, // 线程存活时间
                TimeUnit.MILLISECONDS, // 线程存活时间单位
                new LinkedBlockingQueue<>(), // 任务队列
                getThreadFactory(SINGLE_NAME_STEM)
        );
    }

    public static synchronized ThreadPoolExecutor getCachedThreadPool() {
        if (!isEnableExecutor(cachedThreadPool)) {
            cachedThreadPool = new ThreadPoolExecutor(
                    0, // 核心线程数
                    Integer.MAX_VALUE, // 最大线程数
                    30000, // 线程存活时间
                    TimeUnit.MILLISECONDS, // 线程存活时间单位
                    new SynchronousQueue<>(), // 任务队列
                    getThreadFactory(CACHED_NAME_STEM),
                    (r, executor) -> getCachedThreadPool().execute(r)
            );
        }
        return cachedThreadPool;
    }

    public static synchronized ScheduledThreadPoolExecutor getScheduledThreadPool() {
        if (!isEnableExecutor(scheduledThreadPool)) {
            scheduledThreadPool = new ScheduledThreadPoolExecutor(
                    1, // 核心线程数
                    getThreadFactory(SCHEDULED_NAME_STEM),
                    (r, executor) -> getScheduledThreadPool().execute(r)
            );
        }
        return scheduledThreadPool;
    }

    private static boolean isEnableExecutor(ThreadPoolExecutor executor) {
        return executor != null && !executor.isShutdown() && !executor.isTerminated();
    }

    private static ThreadFactory getThreadFactory(String type) {
        return new ThreadFactory() {

            private final AtomicInteger threadId = new AtomicInteger(0);
            private ThreadGroup group;

            private synchronized ThreadGroup getGroup() {
                if (group == null) {
                    SecurityManager s = System.getSecurityManager();
                    group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                }
                return group;
            }

            @Override
            public Thread newThread(Runnable runnable) {
                Thread t = new Thread(getGroup(), runnable);
                t.setName(String.format(type, threadId.getAndIncrement()));
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        };
    }
}

