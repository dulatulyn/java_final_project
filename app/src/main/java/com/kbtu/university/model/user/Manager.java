package com.kbtu.university.model.user;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.admin.Request;
import com.kbtu.university.model.enums.ManagerTypeEnum;
import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.model.enums.TitleEnum;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;
import java.util.List;

public class Manager extends Employee {

    private static final long serialVersionUID = 1L;

    private TitleEnum title;
    private ManagerTypeEnum type;

    public Manager(String id, String login, String passwordHash,
                   double salary, LocalDate hireDate, String school,
                   TitleEnum title, ManagerTypeEnum type) {
        super(id, login, passwordHash, salary, hireDate, school);
        this.title = title;
        this.type = type;
    }

    public void assignTeacher(Teacher t, Course c) {
        DataStorage.getInstance().linkTeacherToCourse(c.getCode(), t.getId());
        DataStorage.getInstance().log("Manager " + id + " assigned " + t.getId() + " to " + c.getCode());
    }

    public void publishNews(String headline) {
        DataStorage.getInstance().publishNews(headline);
    }

    public List<Request> viewPendingRequests() {
        return DataStorage.getInstance().pendingRequests();
    }

    public boolean signRequest(String requestId) {
        Request r = DataStorage.getInstance().findRequestById(requestId);
        if (r == null) return false;
        r.sign(this.id);
        DataStorage.getInstance().log("Manager " + id + " signed " + requestId);
        return true;
    }

    public boolean rejectRequest(String requestId) {
        Request r = DataStorage.getInstance().findRequestById(requestId);
        if (r == null) return false;
        r.reject(this.id);
        DataStorage.getInstance().log("Manager " + id + " rejected " + requestId);
        return true;
    }

    public static List<Teacher> getInstructorsOf(Course c) {
        return DataStorage.getInstance().findInstructors(c.getCode());
    }

    public static List<Course> getCoursesOf(Teacher t) {
        return DataStorage.getInstance().findCoursesOf(t.getId());
    }

    public TitleEnum getTitle() {
        return title;
    }

    public ManagerTypeEnum getType() {
        return type;
    }

    @Override
    public RoleEnum getRole() {
        return RoleEnum.MANAGER;
    }
}
