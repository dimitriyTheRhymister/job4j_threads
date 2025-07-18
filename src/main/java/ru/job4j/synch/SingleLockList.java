package ru.job4j.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    @GuardedBy("this")
    private final List<T> list;

    public SingleLockList(List<T> list) {
        this.list = copy(list);
    }

    public void add(T value) {
        synchronized (list) {
            list.add(value);
        }
    }

    public T get(int index) {
        synchronized (list) {
            return list.get(index);
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private final List<T> snapshot;
            private int index = 0;

            {
                synchronized (list) {
                    snapshot = copy(list);
                }
            }

            @Override
            public boolean hasNext() {
                return index < snapshot.size();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return snapshot.get(index++);
            }
        };
    }

    private List<T> copy(List<T> origin) {
        synchronized (origin) {
            return new ArrayList<>(origin);
        }
    }
}