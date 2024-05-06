## 收敛线程池
使用ASM将Android项目和SDK中的线程池Hook为统一的线程池进行管理，降低内存使用

### 配置方式
```
// 将Thread、Executors、ThreadPoolExecutor、ScheduledThreadPoolExecutor统一实现
hookThread {
    enableHookThread = true
    hookThread = "com/cw/asm/unifiedthread/OptThread" // 统一Thread
    hookExecutors = "com/cw/asm/unifiedthread/ThreadUtil" // 统一Executors
    hookThreadPoolExecutor = "com/cw/asm/unifiedthread/OptThreadPool" // 统一ThreadPoolExecutor
    hookScheduledThreadPoolExecutor = "com/cw/asm/unifiedthread/OptScheduledThreadPool" // 统一ScheduledThreadPoolExecutor
}
```

### 测试代码
```
/**
 * Hook 线程池
 * 验证:运行后查看class文件是否修改
 * 路径:app/build/tmp/kotlin-classes/debug/com/cw/asm/unifiedthread/MainActivity.class
 * Tools->Kotlin->Decompile to Java 查看反编译字节码
 */
private fun hookThreadPool() {
    // Thread -> OptThread
    Thread {}

    // Executors.newFixedThreadPool() -> ThreadUtil.newFixedThreadPool()
    Executors.newFixedThreadPool(2)
    Executors.newScheduledThreadPool(2)

    // ThreadPoolExecutor() -> OptThreadPool()
    ThreadPoolExecutor(
        Runtime.getRuntime().availableProcessors(),
        36,
        30000,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue(),
        Executors.defaultThreadFactory()
    )

    // TestThreadPool extends ThreadPoolExecutor -> TestThreadPool extends OptThreadPool
    TestThreadPool(
        Runtime.getRuntime().availableProcessors(),
        36,
        30000,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue(),
        Executors.defaultThreadFactory(),
        ThreadPoolExecutor.AbortPolicy()
    )

    // TestScheduledThreadPool extends ScheduledThreadPoolExecutor -> TestThreadPool extends OptScheduledThreadPool
    ScheduledThreadPoolExecutor(
        Runtime.getRuntime().availableProcessors(),
        Executors.defaultThreadFactory(),
    )
}
```

