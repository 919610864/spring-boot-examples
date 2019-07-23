package com.neo.paralle;

/**
 * Created by aa on 2017-08-21.
 */
@FunctionalInterface
public interface FuncWithArgs<T> {
    T doFunc(Object[] args);
}
