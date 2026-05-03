package com.kbtu.university.model.academic;

import com.kbtu.university.model.enums.LessonType;
import com.kbtu.university.model.enums.RoomType;
import com.kbtu.university.model.user.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private int credits;
    private int maxStudents;
    private int yearOfStudy;
    private LessonType lessonType;
    private RoomType requiredRoomType;
    private List<Student> enrolledStudents;
    private List<Lesson> lessons;

    public Course(String code, String name, int credits, int maxStudents, int yearOfStudy,
                  LessonType lessonType, RoomType requiredRoomType) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.maxStudents = maxStudents;
        this.yearOfStudy = yearOfStudy;
        this.lessonType = lessonType;
        this.requiredRoomType = requiredRoomType;
        this.enrolledStudents = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }

    public void addStudent(Student s) {
        if (!enrolledStudents.contains(s)) {
            enrolledStudents.add(s);
        }
    }

    public void addLesson(Lesson l) {
        lessons.add(l);
    }

    public void clearLessons() {
        lessons.clear();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public RoomType getRequiredRoomType() {
        return requiredRoomType;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public int getEnrolledCount() {
        return enrolledStudents.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        return code.equals(((Course) o).code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "Course[" + code + " " + name + ", " + credits + " cr, year " + yearOfStudy + "]";
    }
}
