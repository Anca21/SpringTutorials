package com.ixactsoft.async.mvc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Ovidiu Lupas
 */
@Service
public class AsyncService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AsyncService.class);

    @Async
    public Future<Long> callAsync(int taskCall) throws InterruptedException {

        Stopwatch stopwatch = Stopwatch.createStarted();
        LOGGER.info("task " + taskCall + " starting");
        Thread.sleep(500);
        stopwatch.elapsed(TimeUnit.MILLISECONDS);
        LOGGER.info("task " + taskCall + " completed in " + stopwatch);
        return new AsyncResult<Long>(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Async
    //executed on Spring default async executor - SimpleAsyncTaskExecutor
    public void asyncMethod() {
        LOGGER.info("Execute method asynchronously. " + Thread.currentThread().getName());
    }

    @Async("threadPoolTaskExecutor")
    //executed on configured executor - SimpleAsyncTaskExecutor
    public void asyncMethodConfiguredExecutor() {
        LOGGER.info("Execute method asynchronously. " + Thread.currentThread().getName());
    }

    @Async
    public CompletableFuture<Void> sleep() {
        LOGGER.info("Execute sleep asynchronously. " + Thread.currentThread().getName());
        return CompletableFuture.runAsync(() -> {
            try {
                LOGGER.info("Execute method asynchronously. " + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            };
        } );

    }

    @Async("threadPoolTaskExecutor")
    public void process(DeferredResult<String> result) {
        try {
            TimeUnit.SECONDS.sleep(3);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        result.setResult("Simulating a long running task");
    }
}