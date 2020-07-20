package com.example.test.arraylistrocketmq;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author Chen
 * @version 1.0
 * @date 2020/7/20 9:30
 * @description:
 */

public class FutureTaskExt<V> extends FutureTask<V> {
    private final Runnable runnable;

    public FutureTaskExt(final Callable<V> callable) {
        super(callable);
        this.runnable = null;
    }

    public FutureTaskExt(final Runnable runnable, final V result) {
        super(runnable, result);
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
