package com.kbtu.university.model.academic;

import com.kbtu.university.model.enums.LessonType;
import com.kbtu.university.model.user.Teacher;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    private LessonType type;
    private Room room;
    private LocalDateTime dateTime;
    private Course course;
    private Teacher teacher;

    public Lesson(LessonType type, Room room, LocalDateTime dateTime, Course course, Teacher teacher) {
        this.type = type;
        this.room = room;
        this.dateTime = dateTime;
        this.course = course;
        this.teacher = teacher;
    }

    public boolean isVacancy() {
        return teacher == null;
    }

    public LessonType getType() {
        return type;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Course getCourse() {
        return course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        String teacherName = teacher == null ? "<Vacancy>" : teacher.getLogin();
        return "Lesson[" + course.getCode() + " " + type + " @ " + dateTime
                + " in " + room.getNumber() + " by " + teacherName + "]";
    }
}
