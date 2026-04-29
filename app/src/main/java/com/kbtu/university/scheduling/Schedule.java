package com.kbtu.university.scheduling;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Lesson;

import java.io.Serializable;
import java.util.List;

public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Lesson> lessons;
    private List<Course> unplaced;

    public Schedule(List<Lesson> lessons, List<Course> unplaced) {
        this.lessons = lessons;
        this.unplaced = unplaced;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<Course> getUnplaced() {
        return unplaced;
    }

    public int countVacancies() {
        int count = 0;
        for (Lesson l : lessons) {
            if (l.getTeacher() == null) count++;
        }
        return count;
    }
}
