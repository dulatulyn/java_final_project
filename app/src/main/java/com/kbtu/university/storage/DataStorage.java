package com.kbtu.university.storage;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Mark;
import com.kbtu.university.model.academic.Room;
import com.kbtu.university.model.research.ResearchPaper;
import com.kbtu.university.model.research.ResearchProject;
import com.kbtu.university.model.user.Employee;
import com.kbtu.university.model.user.ResearcherRole;
import com.kbtu.university.model.user.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    private List<String> log;

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
        log.add("Added user " + u.getId() + " (" + u.getRole() + ")");
    }

    public void removeUser(String userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                users.remove(i);
                log.add("Removed user " + userId);
                return;
            }
        }
    }

    public void replaceUser(User u) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(u.getId())) {
                users.set(i, u);
                log.add("Updated user " + u.getId());
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
            log.add("Added researcher wrapping " + r.getWrappedUser().getId());
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
        log.add("News published: " + headline);
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

    public void log(String message) {
        log.add(message);
    }

    public List<String> getLog() {
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
