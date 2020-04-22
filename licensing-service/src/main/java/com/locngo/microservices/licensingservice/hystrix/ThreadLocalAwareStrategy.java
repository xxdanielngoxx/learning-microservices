package com.locngo.microservices.licensingservice.hystrix;

import com.locngo.microservices.licensingservice.utils.UserContextHolder;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy existingHystrixConcurrencyStrategy;

    public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy existingHystrixConcurrencyStrategy) {
        this.existingHystrixConcurrencyStrategy = existingHystrixConcurrencyStrategy;
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return existingHystrixConcurrencyStrategy != null ?
                existingHystrixConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue) :
                super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties threadPoolProperties) {
        return existingHystrixConcurrencyStrategy != null ?
                existingHystrixConcurrencyStrategy.getThreadPool(threadPoolKey, threadPoolProperties) :
                super.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return existingHystrixConcurrencyStrategy != null ?
                existingHystrixConcurrencyStrategy.getBlockingQueue(maxQueueSize) :
                super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return existingHystrixConcurrencyStrategy != null ?
                existingHystrixConcurrencyStrategy.wrapCallable(DelegatingUserContextCallable.create(callable, UserContextHolder.getContext())) :
                super.wrapCallable(DelegatingUserContextCallable.create(callable, UserContextHolder.getContext()));
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return existingHystrixConcurrencyStrategy != null ?
                existingHystrixConcurrencyStrategy.getRequestVariable(rv) :
                super.getRequestVariable(rv);
    }
}
