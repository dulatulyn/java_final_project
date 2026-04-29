package com.kbtu.university.cli;

import com.kbtu.university.exception.CreditLimitException;
import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Lesson;
import com.kbtu.university.model.academic.Mark;
import com.kbtu.university.model.user.Student;
import com.kbtu.university.model.user.Teacher;
import com.kbtu.university.model.user.User;
import com.kbtu.university.storage.DataStorage;

import java.util.Scanner;

public class StudentMenu implements Menu {

    @Override
    public void run(User user, Scanner scanner) {
        Student student = (Student) user;
        DataStorage db = DataStorage.getInstance();

        while (true) {
            System.out.println();
            System.out.println("Student menu");
            System.out.println("1. View my courses");
            System.out.println("2. Register for course");
            System.out.println("3. View marks");
            System.out.println("4. View transcript");
            System.out.println("5. View my schedule");
            System.out.println("6. Rate teacher");
            System.out.println("7. View received news");
            System.out.println("0. Logout");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                student.logout();
                return;
            } else if (choice.equals("1")) {
                for (Course c : student.getCourses()) {
                    System.out.println("  " + c);
                }
            } else if (choice.equals("2")) {
                System.out.print("Course code: ");
                String code = scanner.nextLine().trim();
                Course c = db.findCourseByCode(code);
                if (c == null) {
                    System.out.println("Course not found");
                } else {
                    try {
                        student.registerForCourse(c);
                        System.out.println("Registered for " + c.getCode());
                    } catch (CreditLimitException e) {
                        System.out.println("Failed: " + e.getMessage());
                    }
                }
            } else if (choice.equals("3")) {
                for (Mark m : student.viewMarks()) {
                    System.out.println("  " + m);
                }
            } else if (choice.equals("4")) {
                System.out.println(student.getTranscript());
            } else if (choice.equals("5")) {
                for (Lesson l : student.viewSchedule()) {
                    System.out.println("  " + l);
                }
            } else if (choice.equals("6")) {
                System.out.print("Teacher id: ");
                String tid = scanner.nextLine().trim();
                System.out.print("Rating (1-5): ");
                int r = parseInt(scanner.nextLine().trim(), 0);
                User t = db.findUserById(tid);
                if (t instanceof Teacher) {
                    try {
                        student.rateTeacher((Teacher) t, r);
                        System.out.println("Rated");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Failed: " + e.getMessage());
                    }
                } else {
                    System.out.println("Teacher not found");
                }
            } else if (choice.equals("7")) {
                if (student.getReceivedNews().isEmpty()) {
                    System.out.println("  (no news)");
                }
                for (String n : student.getReceivedNews()) {
                    System.out.println("  " + n);
                }
            } else {
                System.out.println("Unknown option");
            }
        }
    }

    private static int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
