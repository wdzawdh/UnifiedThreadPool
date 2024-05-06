package com.cw.asm.testjar

import androidx.annotation.Keep
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Keep
class TestJarThread {

    fun hookThread() {
        // Thread -> OptThread
        Thread {}

        // Executors.newFixedThreadPool() -> ThreadUtil.newFixedThreadPool()
        Executors.newFixedThreadPool(2)
        Executors.newScheduledThreadPool(2)

        // ThreadPoolExecutor() -> OptThreadPool()
        ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),  // 核心线程数
            36,  // 最大线程数
            30000,  // 线程存活时间
            TimeUnit.MILLISECONDS,  // 线程存活时间单位
            LinkedBlockingQueue(),  // 任务队列
            Executors.defaultThreadFactory()
        )

        // TestScheduledThreadPool extends ScheduledThreadPoolExecutor -> TestThreadPool extends OptScheduledThreadPool
        ScheduledThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),  // 核心线程数
            Executors.defaultThreadFactory(),
        )
    }
}
