package com.kbtu.university.model.admin;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime at;
    private String message;

    public LogEntry(String message) {
        this.at = LocalDateTime.now();
        this.message = message;
    }

    public LocalDateTime getAt() {
        return at;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + at + "] " + message;
    }
}
