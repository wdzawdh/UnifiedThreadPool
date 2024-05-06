package com.cw.asm.unifiedthread;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OptThread extends Thread {

    public OptThread() {
        super();
    }

    public OptThread(@Nullable Runnable target) {
        super(target);
    }

    public OptThread(@Nullable ThreadGroup group, @Nullable Runnable target) {
        super(group, target);
    }

    public OptThread(@NonNull String name) {
        super(name);
    }

    public OptThread(@Nullable ThreadGroup group, @NonNull String name) {
        super(group, name);
    }

    public OptThread(@Nullable Runnable target, @NonNull String name) {
        super(target, name);
    }

    public OptThread(@Nullable ThreadGroup group, @Nullable Runnable target, @NonNull String name) {
        super(group, target, name);
    }

    public OptThread(@Nullable ThreadGroup group, @Nullable Runnable target, @NonNull String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public synchronized void start() {
        ThreadUtil.getThreadPool().execute(OptThread.this);
    }
}
