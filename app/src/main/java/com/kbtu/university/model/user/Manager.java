package com.kbtu.university.model.user;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.enums.ManagerTypeEnum;
import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.model.enums.TitleEnum;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Manager extends Teacher {

    private static final long serialVersionUID = 1L;

    private ManagerTypeEnum type;

    public Manager(String id, String login, String passwordHash,
                   double salary, LocalDate hireDate, String school,
                   TitleEnum title, ManagerTypeEnum type) {
        super(id, login, passwordHash, salary, hireDate, school, title);
        this.type = type;
    }

    public void approveRegistration(Student s, Course c) {
        DataStorage.getInstance().log("Manager " + id + " approved " + s.getId() + " for " + c.getCode());
    }

    public void assignTeacher(Teacher t, Course c) {
        c.addInstructor(t);
        t.addCourse(c);
        DataStorage.getInstance().log("Manager " + id + " assigned " + t.getId() + " to " + c.getCode());
    }

    public String generateReport(List<Student> students) {
        if (students.isEmpty()) {
            return "Report: no students";
        }
        double totalGpa = 0;
        int totalCredits = 0;
        int totalFails = 0;
        for (Student s : students) {
            totalGpa += s.getGpa();
            totalCredits += s.getTotalCredits();
            totalFails += s.getFailCount();
        }
        double avgGpa = totalGpa / students.size();
        return "Report: " + students.size() + " students"
                + ", avg GPA = " + avgGpa
                + ", total credits = " + totalCredits
                + ", total fails = " + totalFails;
    }

    public void publishNews(String headline) {
        DataStorage.getInstance().publishNews(headline);
    }

    public List<Student> viewStudentsByGpa() {
        List<Student> students = collectStudents();
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return Double.compare(b.getGpa(), a.getGpa());
            }
        });
        return students;
    }

    public List<Student> viewStudentsByLogin() {
        List<Student> students = collectStudents();
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return a.getLogin().compareTo(b.getLogin());
            }
        });
        return students;
    }

    private List<Student> collectStudents() {
        List<Student> students = new ArrayList<>();
        for (User u : DataStorage.getInstance().getUsers()) {
            if (u instanceof Student) {
                students.add((Student) u);
            }
        }
        return students;
    }

    public ManagerTypeEnum getType() {
        return type;
    }

    @Override
    public RoleEnum getRole() {
        return RoleEnum.MANAGER;
    }
}
