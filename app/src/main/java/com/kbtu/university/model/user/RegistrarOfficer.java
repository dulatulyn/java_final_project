package com.kbtu.university.model.user;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RegistrarOfficer extends Employee {

    private static final long serialVersionUID = 1L;

    public RegistrarOfficer(String id, String login, String passwordHash,
                            double salary, LocalDate hireDate, String school) {
        super(id, login, passwordHash, salary, hireDate, school);
    }

    public void approveRegistration(Student s, Course c) {
        DataStorage.getInstance().log(
                "Registrar " + id + " approved " + s.getId() + " for " + c.getCode());
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

    @Override
    public RoleEnum getRole() {
        return RoleEnum.REGISTRAR;
    }
}
