package com.neo.paralle;

/**
 * Created by aa on 2017-08-21.
 */
@FunctionalInterface
public interface ActionWithArgs {
    Object[] parameters = null;

    void doAction(Object[] args);
}
