package com.cw.asm.unifiedthread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool extends ThreadPoolExecutor {
    public TestThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public void execute(Runnable command) {
        RunnableEnhance runnable = new RunnableEnhance(command);
        super.execute(runnable);
    }

    protected void beforeExecute(Thread t, Runnable r) {
        if (r instanceof RunnableEnhance) {
            String parentName = ((RunnableEnhance) r).getParentName();
            int parentIndex = parentName.lastIndexOf(" -->");
            if (parentIndex != -1) {
                parentName = parentName + parentIndex + " -->".length();
            }

            String threadName = t.getName();
            int threadIndex = threadName.lastIndexOf(" -->");
            if (threadIndex != -1) {
                threadName = threadName + threadIndex + " -->".length();
            }

            t.setName(parentName + " -->" + threadName);
        }

        super.beforeExecute(t, r);
    }

    public class RunnableEnhance implements Runnable {
        private Runnable proxy;
        private String parentName = Thread.currentThread().getName();

        RunnableEnhance(Runnable runnable) {
            this.proxy = runnable;
        }

        public void run() {
            this.proxy.run();
        }

        public String getParentName() {
            return this.parentName;
        }
    }
}
