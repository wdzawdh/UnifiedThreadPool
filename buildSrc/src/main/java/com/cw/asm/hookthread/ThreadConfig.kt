package com.cw.asm.hookthread

import org.objectweb.asm.Opcodes

fun getThreadMethods(hookThread: String) = listOf(
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/lang/Thread",
        name = "<init>",
        descriptor = "()V",
        hook = hookThread
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/lang/Thread",
        name = "<init>",
        descriptor = "(Ljava/lang/Runnable;)V",
        hook = hookThread
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/lang/Thread",
        name = "<init>",
        descriptor = "(Ljava/lang/Runnable;Ljava/security/AccessControlContext;)V",
        hook = hookThread
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/lang/Thread",
        name = "<init>",
        descriptor = "(Ljava/lang/String;)V",
        hook = hookThread
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/lang/Thread",
        name = "<init>",
        descriptor = "(Ljava/lang/ThreadGroup;Ljava/lang/String;)V",
        hook = hookThread
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/lang/Thread",
        name = "<init>",
        descriptor = "(Ljava/lang/ThreadGroup;Ljava/lang/Runnable;Ljava/lang/String;)V",
        hook = hookThread
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/lang/Thread",
        name = "<init>",
        descriptor = "(Ljava/lang/ThreadGroup;Ljava/lang/Runnable;Ljava/lang/String;J)V",
        hook = hookThread
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/lang/Thread",
        name = "<init>",
        descriptor = "(Ljava/lang/ThreadGroup;Ljava/lang/Runnable;Ljava/lang/String;JZ)V",
        hook = hookThread
    ),
)

fun getThreadPoolMethods(hookThreadPoolExecutor: String) = listOf(
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/util/concurrent/ThreadPoolExecutor",
        name = "<init>",
        descriptor = "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;)V",
        hook = hookThreadPoolExecutor
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/util/concurrent/ThreadPoolExecutor",
        name = "<init>",
        descriptor = "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;)V",
        hook = hookThreadPoolExecutor
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/util/concurrent/ThreadPoolExecutor",
        name = "<init>",
        descriptor = "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/RejectedExecutionHandler;)V",
        hook = hookThreadPoolExecutor
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/util/concurrent/ThreadPoolExecutor",
        name = "<init>",
        descriptor = "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;)V",
        hook = hookThreadPoolExecutor
    )
)

fun getScheduledThreadPoolMethods(hookScheduledThreadPoolExecutor: String) = listOf(
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/util/concurrent/ScheduledThreadPoolExecutor",
        name = "<init>",
        descriptor = "(I)V",
        hook = hookScheduledThreadPoolExecutor
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/util/concurrent/ScheduledThreadPoolExecutor",
        name = "<init>",
        descriptor = "(ILjava/util/concurrent/ThreadFactory;)V",
        hook = hookScheduledThreadPoolExecutor
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/util/concurrent/ScheduledThreadPoolExecutor",
        name = "<init>",
        descriptor = "(ILjava/util/concurrent/RejectedExecutionHandler;)V",
        hook = hookScheduledThreadPoolExecutor
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESPECIAL,
        owner = "java/util/concurrent/ScheduledThreadPoolExecutor",
        name = "<init>",
        descriptor = "(ILjava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;)V",
        hook = hookScheduledThreadPoolExecutor
    )
)

fun getExecutorsMethods(hookThreadPool: String) = listOf(
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newSingleThreadExecutor",
        descriptor = "()Ljava/util/concurrent/ExecutorService;",
        hook = hookThreadPool
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newSingleThreadExecutor",
        descriptor = "(Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
        hook = hookThreadPool
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newFixedThreadPool",
        descriptor = "(I)Ljava/util/concurrent/ExecutorService;",
        hook = hookThreadPool
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newFixedThreadPool",
        descriptor = "(ILjava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
        hook = hookThreadPool
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newCachedThreadPool",
        descriptor = "()Ljava/util/concurrent/ExecutorService;",
        hook = hookThreadPool
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newCachedThreadPool",
        descriptor = "(Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
        hook = hookThreadPool
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newScheduledThreadPool",
        descriptor = "(I)Ljava/util/concurrent/ScheduledExecutorService;",
        hook = hookThreadPool
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newScheduledThreadPool",
        descriptor = "(ILjava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ScheduledExecutorService;",
        hook = hookThreadPool
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newSingleThreadScheduledExecutor",
        descriptor = "()Ljava/util/concurrent/ScheduledExecutorService;",
        hook = hookThreadPool
    ),
    ThreadMethod(
        opcode = Opcodes.INVOKESTATIC,
        owner = "java/util/concurrent/Executors",
        name = "newSingleThreadScheduledExecutor",
        descriptor = "(Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ScheduledExecutorService;",
        hook = hookThreadPool
    )
)
