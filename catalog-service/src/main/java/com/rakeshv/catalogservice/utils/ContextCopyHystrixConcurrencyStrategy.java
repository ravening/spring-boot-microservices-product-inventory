package com.rakeshv.catalogservice.utils;

import java.util.concurrent.Callable;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * ContextCopyHystrixConcurrencyStrategy
 */
@Component
@Slf4j
public class ContextCopyHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    public ContextCopyHystrixConcurrencyStrategy() {
        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new MyCallable(callable, MyThreadLocalsHolder.getCorrelationId());
    }

    public static class MyCallable<T> implements Callable<T> {

        private final Callable<T> actual;
        private final String correlationId;

        public MyCallable(Callable<T> callable, String correlationId) {
            this.actual = callable;
            this.correlationId = correlationId;
        }

        @Override
        public T call() throws Exception {
            log.info("-----------call()------------------");
            MyThreadLocalsHolder.setCorrelationId(correlationId);
            try {
                return actual.call();
            } finally {
                MyThreadLocalsHolder.setCorrelationId(null);
            }
        }
    }
}