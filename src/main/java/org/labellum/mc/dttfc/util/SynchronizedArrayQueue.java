package org.labellum.mc.dttfc.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class SynchronizedArrayQueue<E> implements Queue<E> {
    private final Queue<E> instance = new ArrayDeque<>();

    @Override
    public int size() {
        synchronized (instance) {
            return instance.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (instance) {
            return instance.isEmpty();
        }
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull <T> T[] toArray(@NotNull T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean add(E e) {
        synchronized (instance) {
            return instance.add(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        synchronized (instance) {
            return instance.addAll(c);
        }
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void clear() {
        synchronized (instance) {
            clear();
        }
    }

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public E remove() {
        synchronized (instance) {
            return instance.remove();
        }
    }

    @Override
    public E poll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public E element() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
