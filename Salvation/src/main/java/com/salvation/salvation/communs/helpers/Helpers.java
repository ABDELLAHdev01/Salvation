package com.salvation.salvation.communs.helpers;


public class Helpers {

    private Helpers() {
        throw new IllegalStateException("Utility class");
    }
    public static String stringify(Object obj) {
        return String.valueOf(obj);
    }

}
