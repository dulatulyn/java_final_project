package com.kbtu.university.scheduling;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Lesson;
import com.kbtu.university.model.academic.Room;

import java.time.LocalDateTime;
import java.util.List;

public class RoomTypeConstraint implements Constraint {

    @Override
    public boolean check(Course course, Room room, LocalDateTime slot, List<Lesson> placed) {
        return room.getType() == course.getRequiredRoomType();
    }
}
