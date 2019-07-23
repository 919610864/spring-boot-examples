package com.neo.springlisten;

import java.io.Serializable;

public class HeaderItem implements Serializable {
    private static final long serialVersionUID = 4149644303053657084L;
    private String name;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
