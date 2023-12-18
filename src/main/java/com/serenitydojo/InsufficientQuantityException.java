package com.serenitydojo;

import java.io.IOException;

public class InsufficientQuantityException extends IOException {
    public InsufficientQuantityException(String message) {
        super(message);
    }
}
