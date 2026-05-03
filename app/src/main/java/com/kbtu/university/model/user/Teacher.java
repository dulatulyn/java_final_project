package com.kbtu.university.model.user;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Mark;
import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.model.enums.TitleEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Employee {

    private static final long serialVersionUID = 1L;

    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;

    protected TitleEnum title;
    protected List<Integer> ratings;

    public Teacher(String id, String login, String passwordHash,
                   double salary, LocalDate hireDate, String school,
                   TitleEnum title) {
        super(id, login, passwordHash, salary, hireDate, school);
        this.title = title;
        this.ratings = new ArrayList<>();
    }

    public void putMark(Student student, Course course, Mark mark) {
        student.addMark(mark);
    }

    public List<Student> viewStudents() {
        List<Student> result = new ArrayList<>();
        for (Course c : Manager.getCoursesOf(this)) {
            for (Student s : c.getEnrolledStudents()) {
                if (!result.contains(s)) {
                    result.add(s);
                }
            }
        }
        return result;
    }

    public void sendComplaint(Manager to, String text) {
        System.out.println("[complaint " + this.id + " -> manager " + to.getId() + "] " + text);
    }

    public void addRating(int rating) {
        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new IllegalArgumentException("Rating must be in " + MIN_RATING + ".." + MAX_RATING);
        }
        ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) return 0;
        int sum = 0;
        for (int r : ratings) sum += r;
        return (double) sum / ratings.size();
    }

    public boolean isProfessor() {
        return title == TitleEnum.PROFESSOR;
    }

    public TitleEnum getTitle() {
        return title;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setTitle(TitleEnum title) {
        this.title = title;
    }

    @Override
    public RoleEnum getRole() {
        return RoleEnum.TEACHER;
    }
}
