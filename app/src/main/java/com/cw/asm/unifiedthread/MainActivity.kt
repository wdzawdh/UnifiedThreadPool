package com.cw.asm.unifiedthread

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cw.asm.testjar.TestJarThread
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hookThread()
        TestJarThread().hookThread()
    }

    /**
     * hook 线程池
     * 验证:运行后查看class文件是否修改
     * 路径:app/build/tmp/kotlin-classes/debug/com/cw/asm/unifiedthread/MainActivity.class
     * Tools->Kotlin->Decompile to Java 查看反编译字节码
     */
    private fun hookThread() {
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

        // TestThreadPool extends ThreadPoolExecutor -> TestThreadPool extends OptThreadPool
        TestThreadPool(
            Runtime.getRuntime().availableProcessors(),  // 核心线程数
            36,  // 最大线程数
            30000,  // 线程存活时间
            TimeUnit.MILLISECONDS,  // 线程存活时间单位
            LinkedBlockingQueue(),  // 任务队列
            Executors.defaultThreadFactory(),
            ThreadPoolExecutor.AbortPolicy()
        )

        // TestScheduledThreadPool extends ScheduledThreadPoolExecutor -> TestThreadPool extends OptScheduledThreadPool
        ScheduledThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),  // 核心线程数
            Executors.defaultThreadFactory(),
        )
    }
}
