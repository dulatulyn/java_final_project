package com.kbtu.university.model.academic;

import com.kbtu.university.model.enums.RoomType;

import java.io.Serializable;

public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    private String number;
    private int capacity;
    private RoomType type;

    public Room(String number, int capacity, RoomType type) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.number = number;
        this.capacity = capacity;
        this.type = type;
    }

    public boolean isAvailableAt(java.time.LocalDateTime when, java.util.List<Lesson> takenLessons) {
        for (Lesson l : takenLessons) {
            if (l.getRoom().equals(this) && l.getDateTime().equals(when)) {
                return false;
            }
        }
        return true;
    }

    public String getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public RoomType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        return number.equals(((Room) o).number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public String toString() {
        return "Room[" + number + ", " + type + ", capacity=" + capacity + "]";
    }
}
