package com.kbtu.university.factory;

import com.kbtu.university.model.enums.ManagerTypeEnum;
import com.kbtu.university.model.enums.TitleEnum;
import com.kbtu.university.model.user.Admin;
import com.kbtu.university.model.user.Employee;
import com.kbtu.university.model.user.Manager;
import com.kbtu.university.model.user.Student;
import com.kbtu.university.model.user.Teacher;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;

public class UserFactory {

    private static int nextStudentId = 1;
    private static int nextTeacherId = 1;
    private static int nextManagerId = 1;
    private static int nextAdminId = 1;

    private UserFactory() {}

    public static Admin createAdmin(String login, String password) {
        Admin a = new Admin(nextId("A", nextAdminId++), login, password);
        DataStorage.getInstance().addUser(a);
        return a;
    }

    public static Teacher createTeacher(String login, String password,
                                        double salary, LocalDate hireDate, String school,
                                        TitleEnum title) {
        Teacher t = new Teacher(nextId("T", nextTeacherId++), login, password, salary, hireDate, school, title);
        DataStorage.getInstance().addUser(t);
        return t;
    }

    public static Manager createManager(String login, String password,
                                        double salary, LocalDate hireDate, String school,
                                        TitleEnum title, ManagerTypeEnum type) {
        Manager m = new Manager(nextId("M", nextManagerId++), login, password, salary, hireDate, school, title, type);
        DataStorage.getInstance().addUser(m);
        return m;
    }

    public static Student createStudent(String login, String password, String major, int year) {
        Student s = new Student(nextId("S", nextStudentId++), login, password, major, year);
        DataStorage.getInstance().addUser(s);
        return s;
    }

    public static Employee createEmployee(String login, String password,
                                          double salary, LocalDate hireDate, String school) {
        Employee e = new Employee(nextId("E", nextTeacherId++), login, password, salary, hireDate, school);
        DataStorage.getInstance().addUser(e);
        return e;
    }

    private static String nextId(String prefix, int n) {
        return String.format("%s%03d", prefix, n);
    }
}
