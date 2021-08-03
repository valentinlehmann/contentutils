package de.valentinlehmann.contentutils.core;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncRepository<T, K> {
    void add(T t);

    void contains(K key, CompletableFuture<Boolean> result);

    void getNames(CompletableFuture<List<String>> nameList);

    void get(K key, CompletableFuture<T> result);

    void update(K key, T t);

    void remove(K key);
}
