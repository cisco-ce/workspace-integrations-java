package com.cisco.workspaceintegrations.api.caching;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;

import static com.cisco.workspaceintegrations.common.Utils.sneakyThrow;

public abstract class InMemoryCache<T> {

    private final Cache<String, T> cache;

    public InMemoryCache() {
        this(Duration.ofMinutes(5), 10000);
    }

    public InMemoryCache(Duration ttl, int maxSize) {
        this.cache = CacheBuilder.newBuilder()
                                 .recordStats()
                                 .maximumSize(maxSize)
                                 .expireAfterWrite(ttl)
                                 .build();
    }

    public T get(String key) {
        try {
            return this.cache.get(key, () -> load(key));
        } catch (UncheckedExecutionException | ExecutionException e) {
            return sneakyThrow(e);
        }
    }

    protected abstract T load(String key);
}
