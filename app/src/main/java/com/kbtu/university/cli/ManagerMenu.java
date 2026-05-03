package com.kbtu.university.cli;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.admin.Request;
import com.kbtu.university.model.enums.LessonType;
import com.kbtu.university.model.enums.RoomType;
import com.kbtu.university.model.user.Manager;
import com.kbtu.university.model.user.Teacher;
import com.kbtu.university.model.user.User;
import com.kbtu.university.storage.DataStorage;

import java.util.Scanner;

public class ManagerMenu implements Menu {

    @Override
    public void run(User user, Scanner scanner) {
        Manager manager = (Manager) user;
        DataStorage db = DataStorage.getInstance();

        while (true) {
            System.out.println();
            System.out.println("Manager menu (" + manager.getType() + ")");
            System.out.println("1. Assign teacher to course");
            System.out.println("2. Add course for registration");
            System.out.println("3. Publish news");
            System.out.println("4. View pending requests");
            System.out.println("5. Sign request");
            System.out.println("6. Reject request");
            System.out.println("7. View top cited researchers of school");
            System.out.println("8. View top cited papers of year");
            System.out.println("9. View all startups");
            System.out.println("0. Logout");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                manager.logout();
                return;
            } else if (choice.equals("1")) {
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
            } else if (choice.equals("2")) {
                addCourse(scanner, db);
            } else if (choice.equals("3")) {
                System.out.print("Headline: ");
                String headline = scanner.nextLine();
                manager.publishNews(headline);
                System.out.println("News published to " + db.getUsers().size() + " accounts");
            } else if (choice.equals("4")) {
                if (manager.viewPendingRequests().isEmpty()) {
                    System.out.println("  (no pending requests)");
                }
                for (Request r : manager.viewPendingRequests()) {
                    System.out.println("  " + r);
                }
            } else if (choice.equals("5")) {
                System.out.print("Request id: ");
                String rid = scanner.nextLine().trim();
                System.out.println(manager.signRequest(rid) ? "Signed" : "Request not found");
            } else if (choice.equals("6")) {
                System.out.print("Request id: ");
                String rid = scanner.nextLine().trim();
                System.out.println(manager.rejectRequest(rid) ? "Rejected" : "Request not found");
            } else if (choice.equals("7")) {
                System.out.print("School: ");
                String school = scanner.nextLine().trim();
                for (com.kbtu.university.model.user.ResearcherRole r : db.topCitedResearchersOfSchool(school, 10)) {
                    System.out.println("  " + r.getId() + " " + r.getLogin()
                            + " citations=" + r.totalCitations() + " hIndex=" + r.getHIndex());
                }
            } else if (choice.equals("8")) {
                System.out.print("Year: ");
                int y = parseInt(scanner.nextLine().trim(), 2025);
                for (com.kbtu.university.model.research.ResearchPaper p : db.topCitedPapersOfYear(y, 10)) {
                    System.out.println("  " + p);
                }
            } else if (choice.equals("9")) {
                if (db.getStartups().isEmpty()) {
                    System.out.println("  (no startups)");
                }
                for (com.kbtu.university.model.startup.Startup s : db.getStartups()) {
                    System.out.println("  " + s + " desc=\"" + s.getDescription() + "\"");
                }
            } else {
                System.out.println("Unknown option");
            }
        }
    }

    private void addCourse(Scanner scanner, DataStorage db) {
        System.out.print("Code: ");
        String code = scanner.nextLine().trim();
        if (db.findCourseByCode(code) != null) {
            System.out.println("Course with this code already exists");
            return;
        }
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Credits: ");
        int credits = parseInt(scanner.nextLine().trim(), 3);
        System.out.print("Max students: ");
        int maxStudents = parseInt(scanner.nextLine().trim(), 30);
        System.out.print("Year of study (1-4): ");
        int year = parseInt(scanner.nextLine().trim(), 1);
        System.out.println("Lesson type: 1=LECTURE 2=PRACTICE");
        System.out.print("> ");
        int lt = parseInt(scanner.nextLine().trim(), 1);
        LessonType lessonType = lt == 2 ? LessonType.PRACTICE : LessonType.LECTURE;
        System.out.println("Required room: 1=LECTURE_HALL 2=LAB 3=SEMINAR");
        System.out.print("> ");
        int rt = parseInt(scanner.nextLine().trim(), 1);
        RoomType roomType = rt == 2 ? RoomType.LAB : (rt == 3 ? RoomType.SEMINAR : RoomType.LECTURE_HALL);
        Course c = new Course(code, name, credits, maxStudents, year, lessonType, roomType);
        db.addCourse(c);
        db.log("Course " + code + " added for year " + year);
        System.out.println("Course added");
    }

    private static int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
