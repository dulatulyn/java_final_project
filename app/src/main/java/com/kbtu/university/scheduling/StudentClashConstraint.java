package com.kbtu.university.scheduling;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Lesson;
import com.kbtu.university.model.academic.Room;
import com.kbtu.university.model.user.Student;

import java.time.LocalDateTime;
import java.util.List;

public class StudentClashConstraint implements Constraint {

    @Override
    public boolean check(Course course, Room room, LocalDateTime slot, List<Lesson> placed) {
        List<Student> mine = course.getEnrolledStudents();
        for (Lesson l : placed) {
            if (!l.getDateTime().equals(slot)) continue;
            for (Student s : l.getCourse().getEnrolledStudents()) {
                if (mine.contains(s)) return false;
            }
        }
        return true;
    }
}
