package de.valentinlehmann.contentutils.core;

import java.util.Collection;

public interface Repository<T, K> {
    default void startup() {

    }

    default void shutdown() {

    }

    void add(T t);

    boolean contains(K key);

    Collection<T> getAll();

    T get(K key);

    void update(K key, T t);

    void remove(K key);
}
