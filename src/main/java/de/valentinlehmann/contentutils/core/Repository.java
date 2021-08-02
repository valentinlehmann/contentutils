package de.valentinlehmann.contentutils.core;

import java.util.Collection;

public interface Repository<T, K> {
    void add(T t);

    Collection<T> getAll();

    T get(K key);

    void update(K key, T t);

    void remove(K key);
}
