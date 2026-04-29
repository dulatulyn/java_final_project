package com.kbtu.university.scheduling;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Lesson;
import com.kbtu.university.model.academic.Room;

import java.time.LocalDateTime;
import java.util.List;

public interface Constraint {

    boolean check(Course course, Room room, LocalDateTime slot, List<Lesson> placed);
}
