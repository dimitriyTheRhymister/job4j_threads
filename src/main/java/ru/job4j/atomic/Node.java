package ru.job4j.atomic;

/*
Immutable class representing a node in a linked structure.
This class is thread-safe as long as the parameter type T is also immutable.
@param <T> the type of the value stored in the node. Should be immutable.
*/
public final class Node<T> {
    private final Node<T> next;
    private final T value;

    public Node(T value, Node<T> next) {
        this.value = value;
        this.next = next;
    }

    public Node<T> getNext() {
        return next;
    }

    public T getValue() {
        return value;
    }

    public Node<T> withValue(T newValue) {
        return new Node<>(newValue, this.next);
    }

    public Node<T> withNext(Node<T> newNext) {
        return new Node<>(this.value, newNext);
    }
}
