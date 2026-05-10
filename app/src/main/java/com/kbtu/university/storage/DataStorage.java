package com.kbtu.university.storage;

import com.kbtu.university.model.academic.Attendance;
import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Mark;
import com.kbtu.university.model.academic.Room;
import com.kbtu.university.model.admin.LogEntry;
import com.kbtu.university.model.admin.Request;
import com.kbtu.university.model.enums.RequestStatusEnum;
import com.kbtu.university.model.research.ResearchPaper;
import com.kbtu.university.model.research.ResearchProject;
import com.kbtu.university.model.startup.Startup;
import com.kbtu.university.model.user.Employee;
import com.kbtu.university.model.user.ResearcherRole;
import com.kbtu.university.model.user.Teacher;
import com.kbtu.university.model.user.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    private static DataStorage instance;

    private List<User> users;
    private List<ResearcherRole> researchers;
    private List<Course> courses;
    private List<Room> rooms;
    private List<Mark> marks;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private List<String> news;
    private List<LogEntry> log;
    private Map<String, List<String>> teacherAssignments;
    private List<Request> requests;
    private int nextRequestId;
    private List<Attendance> attendances;
    private List<Startup> startups;
    private int nextStartupId;

    private transient List<NewsObserver> newsObservers;

    private DataStorage() {
        this.users = new ArrayList<>();
        this.researchers = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.news = new ArrayList<>();
        this.log = new ArrayList<>();
        this.teacherAssignments = new HashMap<>();
        this.requests = new ArrayList<>();
        this.nextRequestId = 1;
        this.attendances = new ArrayList<>();
        this.startups = new ArrayList<>();
        this.nextStartupId = 1;
        this.newsObservers = new ArrayList<>();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public void addUser(User u) {
        for (User existing : users) {
            if (existing.getId().equals(u.getId())) {
                return;
            }
        }
        users.add(u);
        log.add(new LogEntry("Added user " + u.getId() + " (" + u.getRole() + ")"));
    }

    public void removeUser(String userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                users.remove(i);
                log.add(new LogEntry("Removed user " + userId));
                return;
            }
        }
    }

    public void replaceUser(User u) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(u.getId())) {
                users.set(i, u);
                log.add(new LogEntry("Updated user " + u.getId()));
                return;
            }
        }
    }

    public User findUserByLogin(String login) {
        for (User u : users) {
            if (u.getLogin().equals(login)) return u;
        }
        return null;
    }

    public User findUserById(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) return u;
        }
        return null;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addResearcher(ResearcherRole r) {
        if (!researchers.contains(r)) {
            researchers.add(r);
            log.add(new LogEntry("Added researcher wrapping " + r.getWrappedUser().getId()));
        }
    }

    public List<ResearcherRole> getResearchers() {
        return researchers;
    }

    public void addCourse(Course c) {
        for (Course existing : courses) {
            if (existing.getCode().equals(c.getCode())) return;
        }
        courses.add(c);
    }

    public Course findCourseByCode(String code) {
        for (Course c : courses) {
            if (c.getCode().equals(code)) return c;
        }
        return null;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addRoom(Room r) {
        rooms.add(r);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void addMark(Mark m) {
        marks.add(m);
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void addPaper(ResearchPaper p) {
        if (!papers.contains(p)) {
            papers.add(p);
        }
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    public void addProject(ResearchProject p) {
        projects.add(p);
    }

    public List<ResearchProject> getProjects() {
        return projects;
    }

    public void publishNews(String headline) {
        news.add(headline);
        if (newsObservers == null) {
            newsObservers = new ArrayList<>();
        }
        for (NewsObserver o : newsObservers) {
            o.onNewsPublished(headline);
        }
        log.add(new LogEntry("News published: " + headline));
    }

    public void subscribeToNews(NewsObserver o) {
        if (newsObservers == null) {
            newsObservers = new ArrayList<>();
        }
        if (!newsObservers.contains(o)) {
            newsObservers.add(o);
        }
    }

    public void unsubscribeFromNews(NewsObserver o) {
        if (newsObservers != null) {
            newsObservers.remove(o);
        }
    }

    public List<String> getNews() {
        return news;
    }

    public void linkTeacherToCourse(String courseCode, String teacherId) {
        List<String> ids = teacherAssignments.get(courseCode);
        if (ids == null) {
            ids = new ArrayList<>();
            teacherAssignments.put(courseCode, ids);
        }
        if (!ids.contains(teacherId)) {
            ids.add(teacherId);
        }
    }

    public List<Teacher> findInstructors(String courseCode) {
        List<Teacher> result = new ArrayList<>();
        List<String> ids = teacherAssignments.get(courseCode);
        if (ids == null) return result;
        for (String tid : ids) {
            User u = findUserById(tid);
            if (u instanceof Teacher) {
                result.add((Teacher) u);
            }
        }
        return result;
    }

    public List<Course> findCoursesOf(String teacherId) {
        List<Course> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : teacherAssignments.entrySet()) {
            if (e.getValue().contains(teacherId)) {
                Course c = findCourseByCode(e.getKey());
                if (c != null) result.add(c);
            }
        }
        return result;
    }

    public Request submitRequest(String submitterId, String text) {
        String id = String.format("REQ%03d", nextRequestId++);
        Request r = new Request(id, submitterId, text);
        requests.add(r);
        log.add(new LogEntry("Request " + id + " submitted by " + submitterId));
        return r;
    }

    public Request findRequestById(String id) {
        for (Request r : requests) {
            if (r.getId().equals(id)) return r;
        }
        return null;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<Request> pendingRequests() {
        List<Request> result = new ArrayList<>();
        for (Request r : requests) {
            if (r.getStatus() == RequestStatusEnum.PENDING) {
                result.add(r);
            }
        }
        return result;
    }

    public void addAttendance(Attendance a) {
        attendances.add(a);
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public List<Attendance> findAttendanceByStudent(String studentId) {
        List<Attendance> result = new ArrayList<>();
        for (Attendance a : attendances) {
            if (a.getStudentId().equals(studentId)) result.add(a);
        }
        return result;
    }

    public List<Attendance> findAttendanceByCourse(String courseCode) {
        List<Attendance> result = new ArrayList<>();
        for (Attendance a : attendances) {
            if (a.getCourseCode().equals(courseCode)) result.add(a);
        }
        return result;
    }

    public Startup createStartup(User founder, String name, String description) {
        String id = String.format("ST%03d", nextStartupId++);
        Startup s = new Startup(founder, id, name, description);
        startups.add(s);
        log.add(new LogEntry("Startup " + id + " founded by " + founder.getId()));
        return s;
    }

    public Startup findStartupById(String id) {
        for (Startup s : startups) {
            if (s.getStartupId().equals(id)) return s;
        }
        return null;
    }

    public List<Startup> getStartups() {
        return startups;
    }

    public List<Startup> findStartupsByMember(String userId) {
        List<Startup> result = new ArrayList<>();
        for (Startup s : startups) {
            if (s.getMemberIds().contains(userId)) result.add(s);
        }
        return result;
    }

    public void log(String message) {
        log.add(new LogEntry(message));
    }

    public List<LogEntry> getLog() {
        return log;
    }

    public List<ResearcherRole> topCitedResearchersOfSchool(String school, int limit) {
        List<ResearcherRole> filtered = new ArrayList<>();
        for (ResearcherRole r : researchers) {
            User wrapped = r.getWrappedUser();
            if (wrapped instanceof Employee && school.equals(((Employee) wrapped).getSchool())) {
                filtered.add(r);
            }
        }
        filtered.sort(new Comparator<ResearcherRole>() {
            @Override
            public int compare(ResearcherRole a, ResearcherRole b) {
                return Integer.compare(b.totalCitations(), a.totalCitations());
            }
        });
        if (limit > 0 && limit < filtered.size()) {
            return new ArrayList<>(filtered.subList(0, limit));
        }
        return filtered;
    }

    public List<ResearchPaper> topCitedPapersOfYear(int year, int limit) {
        List<ResearchPaper> filtered = new ArrayList<>();
        for (ResearchPaper p : papers) {
            if (p.getDatePublished() != null && p.getDatePublished().getYear() == year) {
                filtered.add(p);
            }
        }
        filtered.sort(new Comparator<ResearchPaper>() {
            @Override
            public int compare(ResearchPaper a, ResearchPaper b) {
                return Integer.compare(b.getCitations(), a.getCitations());
            }
        });
        if (limit > 0 && limit < filtered.size()) {
            return new ArrayList<>(filtered.subList(0, limit));
        }
        return filtered;
    }

    public void save(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public static void load(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            instance = (DataStorage) ois.readObject();
            instance.newsObservers = new ArrayList<>();
        }
    }
}
