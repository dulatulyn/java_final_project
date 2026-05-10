package com.kbtu.university.model.user;

import com.kbtu.university.exception.CreditLimitException;
import com.kbtu.university.exception.LowHIndexException;
import com.kbtu.university.model.academic.Attendance;
import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Lesson;
import com.kbtu.university.model.academic.Mark;
import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.storage.DataStorage;
import com.kbtu.university.storage.NewsObserver;

import java.util.ArrayList;
import java.util.List;

public class Student extends User implements NewsObserver {

    private static final long serialVersionUID = 1L;

    public static final int MAX_CREDITS = 21;
    public static final int MAX_FAILS = 3;
    public static final int MIN_SUPERVISOR_H_INDEX = 3;
    public static final int MIN_YEAR = 1;
    public static final int MAX_YEAR = 7;

    private double gpa;
    private int year;
    private String major;
    private int totalCredits;
    private int failCount;
    private ResearcherRole supervisor;
    private List<Course> courses;
    private List<Mark> marks;
    private List<String> receivedNews;

    public Student(String id, String login, String passwordHash,
                   String major, int year) {
        super(id, login, passwordHash);
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new IllegalArgumentException("year must be in " + MIN_YEAR + ".." + MAX_YEAR);
        }
        this.major = major;
        this.year = year;
        this.gpa = 0;
        this.totalCredits = 0;
        this.failCount = 0;
        this.supervisor = null;
        this.courses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.receivedNews = new ArrayList<>();
    }

    public void registerForCourse(Course c) throws CreditLimitException {
        if (totalCredits + c.getCredits() > MAX_CREDITS) {
            throw new CreditLimitException(
                    "Student " + id + " cannot register for " + c.getCode()
                            + ": credit limit " + MAX_CREDITS + " would be exceeded");
        }
        courses.add(c);
        c.addStudent(this);
        totalCredits += c.getCredits();
    }

    public List<Mark> viewMarks() {
        return marks;
    }

    public String getTranscript() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transcript for ").append(id).append(" (").append(major).append(", year ").append(year).append(")\n");
        for (Mark m : marks) {
            sb.append("  ").append(m.getCourseCode()).append(": ").append(m.getTotal())
                    .append(" (").append(m.getLetterGrade()).append(")\n");
        }
        return sb.toString();
    }

    public List<Lesson> viewSchedule() {
        List<Lesson> result = new ArrayList<>();
        for (Course c : courses) {
            result.addAll(c.getLessons());
        }
        return result;
    }

    public void rateTeacher(Teacher t, int rating) {
        t.addRating(rating);
        DataStorage.getInstance().log("Student " + id + " rated " + t.getId() + " with " + rating);
    }

    public List<Attendance> viewMyAttendance() {
        return DataStorage.getInstance().findAttendanceByStudent(this.id);
    }

    public com.kbtu.university.model.startup.Startup foundStartup(String name, String description) {
        return DataStorage.getInstance().createStartup(this, name, description);
    }

    public boolean joinStartup(String startupId) {
        com.kbtu.university.model.startup.Startup s = DataStorage.getInstance().findStartupById(startupId);
        if (s == null) return false;
        boolean added = s.addMember(this.id);
        if (added) {
            DataStorage.getInstance().log("Student " + id + " joined startup " + startupId);
        }
        return added;
    }

    public List<com.kbtu.university.model.startup.Startup> viewMyStartups() {
        return DataStorage.getInstance().findStartupsByMember(this.id);
    }

    public void setSupervisor(ResearcherRole r) throws LowHIndexException {
        if (r.getHIndex() < MIN_SUPERVISOR_H_INDEX) {
            throw new LowHIndexException(
                    "Supervisor h-index " + r.getHIndex() + " is below minimum " + MIN_SUPERVISOR_H_INDEX);
        }
        this.supervisor = r;
    }

    public void addMark(Mark m) {
        marks.add(m);
        if (m.getTotal() < 50) {
            failCount++;
        }
        recomputeGpa();
    }

    private void recomputeGpa() {
        if (marks.isEmpty()) {
            gpa = 0;
            return;
        }
        double sum = 0;
        for (Mark m : marks) {
            sum += m.getTotal();
        }
        gpa = sum / marks.size() / 25.0;
    }

    @Override
    public void onNewsPublished(String headline) {
        receivedNews.add(headline);
    }

    public double getGpa() {
        return gpa;
    }

    public int getYear() {
        return year;
    }

    public String getMajor() {
        return major;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public int getFailCount() {
        return failCount;
    }

    public ResearcherRole getSupervisor() {
        return supervisor;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<String> getReceivedNews() {
        return receivedNews;
    }

    public void setYear(int year) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new IllegalArgumentException("year must be in " + MIN_YEAR + ".." + MAX_YEAR);
        }
        this.year = year;
    }

    @Override
    public RoleEnum getRole() {
        return RoleEnum.STUDENT;
    }
}
