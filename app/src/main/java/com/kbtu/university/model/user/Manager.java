package com.kbtu.university.model.user;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.admin.Request;
import com.kbtu.university.model.enums.ManagerTypeEnum;
import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.model.enums.TitleEnum;
import com.kbtu.university.scheduling.Constraint;
import com.kbtu.university.scheduling.Schedule;
import com.kbtu.university.scheduling.Scheduler;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Manager extends Employee {

    private static final long serialVersionUID = 1L;

    private static final LocalDate SCHEDULE_WEEK_START = LocalDate.of(2026, 1, 5);
    private static final int SCHEDULE_PAIRS_PER_DAY = 8;
    private static final int SCHEDULE_RESTARTS = 30;
    private static final long SCHEDULE_SEED = 42L;

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

    public Schedule generateSchedule() {
        DataStorage db = DataStorage.getInstance();
        List<LocalDateTime> slots = Scheduler.buildWeek(SCHEDULE_WEEK_START, SCHEDULE_PAIRS_PER_DAY);
        List<Constraint> constraints = Scheduler.defaultConstraints();
        Schedule schedule = Scheduler.multiStart(
                db.getCourses(), db.getRooms(), slots, constraints,
                SCHEDULE_RESTARTS, SCHEDULE_SEED);
        db.log("Manager " + id + " generated schedule: placed=" + schedule.getLessons().size()
                + " vacancies=" + schedule.countVacancies()
                + " unplaced=" + schedule.getUnplaced().size());
        return schedule;
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
