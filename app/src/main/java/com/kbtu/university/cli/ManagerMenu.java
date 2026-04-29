package com.kbtu.university.cli;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Room;
import com.kbtu.university.model.user.Manager;
import com.kbtu.university.model.user.Student;
import com.kbtu.university.model.user.Teacher;
import com.kbtu.university.model.user.User;
import com.kbtu.university.scheduling.Constraint;
import com.kbtu.university.scheduling.Schedule;
import com.kbtu.university.scheduling.Scheduler;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ManagerMenu implements Menu {

    @Override
    public void run(User user, Scanner scanner) {
        Manager manager = (Manager) user;
        DataStorage db = DataStorage.getInstance();

        while (true) {
            System.out.println();
            System.out.println("Manager menu (" + manager.getType() + ")");
            System.out.println("1. View students by GPA");
            System.out.println("2. View students alphabetically");
            System.out.println("3. Approve registration");
            System.out.println("4. Assign teacher to course");
            System.out.println("5. Generate report");
            System.out.println("6. Publish news");
            System.out.println("7. Run scheduler");
            System.out.println("8. View top cited researchers of school");
            System.out.println("9. View top cited papers of year");
            System.out.println("0. Logout");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                manager.logout();
                return;
            } else if (choice.equals("1")) {
                printStudents(manager.viewStudentsByGpa());
            } else if (choice.equals("2")) {
                printStudents(manager.viewStudentsByLogin());
            } else if (choice.equals("3")) {
                System.out.print("Student id: ");
                String sid = scanner.nextLine().trim();
                System.out.print("Course code: ");
                String code = scanner.nextLine().trim();
                User s = db.findUserById(sid);
                Course c = db.findCourseByCode(code);
                if (s instanceof Student && c != null) {
                    manager.approveRegistration((Student) s, c);
                    System.out.println("Approved");
                } else {
                    System.out.println("Student or course not found");
                }
            } else if (choice.equals("4")) {
                System.out.print("Teacher id: ");
                String tid = scanner.nextLine().trim();
                System.out.print("Course code: ");
                String code = scanner.nextLine().trim();
                User t = db.findUserById(tid);
                Course c = db.findCourseByCode(code);
                if (t instanceof Teacher && c != null) {
                    manager.assignTeacher((Teacher) t, c);
                    System.out.println("Assigned");
                } else {
                    System.out.println("Teacher or course not found");
                }
            } else if (choice.equals("5")) {
                System.out.println(manager.generateReport(manager.viewStudentsByGpa()));
            } else if (choice.equals("6")) {
                System.out.print("Headline: ");
                String headline = scanner.nextLine();
                manager.publishNews(headline);
                System.out.println("News published to " + db.getUsers().size() + " accounts");
            } else if (choice.equals("7")) {
                List<Course> courses = db.getCourses();
                List<Room> rooms = db.getRooms();
                List<LocalDateTime> slots = Scheduler.buildWeek(LocalDate.of(2026, 1, 5), 8);
                List<Constraint> constraints = Scheduler.defaultConstraints();
                Schedule schedule = Scheduler.multiStart(courses, rooms, slots, constraints, 30, 42L);
                printSchedule(schedule);
            } else if (choice.equals("8")) {
                System.out.print("School: ");
                String school = scanner.nextLine().trim();
                for (com.kbtu.university.model.user.ResearcherRole r : db.topCitedResearchersOfSchool(school, 10)) {
                    System.out.println("  " + r.getId() + " " + r.getLogin()
                            + " citations=" + r.totalCitations() + " hIndex=" + r.getHIndex());
                }
            } else if (choice.equals("9")) {
                System.out.print("Year: ");
                int y = parseInt(scanner.nextLine().trim(), 2025);
                for (com.kbtu.university.model.research.ResearchPaper p : db.topCitedPapersOfYear(y, 10)) {
                    System.out.println("  " + p);
                }
            } else {
                System.out.println("Unknown option");
            }
        }
    }

    private void printStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("  (no students)");
            return;
        }
        for (Student s : students) {
            System.out.println("  " + s.getId() + " " + s.getLogin()
                    + " gpa=" + s.getGpa() + " year=" + s.getYear());
        }
    }

    private void printSchedule(Schedule schedule) {
        for (com.kbtu.university.model.academic.Lesson l : schedule.getLessons()) {
            String teacherName = l.getTeacher() == null ? "<Vacancy>" : l.getTeacher().getLogin();
            System.out.printf("  %s  %-22s  room=%-5s  teacher=%s%n",
                    l.getDateTime(), l.getCourse().getName(), l.getRoom().getNumber(), teacherName);
        }
        System.out.println("Placed: " + schedule.getLessons().size()
                + ", Vacancies: " + schedule.countVacancies()
                + ", Unplaced: " + schedule.getUnplaced().size());
    }

    private static int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
