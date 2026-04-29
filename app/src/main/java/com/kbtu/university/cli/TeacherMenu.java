package com.kbtu.university.cli;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Mark;
import com.kbtu.university.model.user.Manager;
import com.kbtu.university.model.user.Student;
import com.kbtu.university.model.user.Teacher;
import com.kbtu.university.model.user.User;
import com.kbtu.university.storage.DataStorage;

import java.util.List;
import java.util.Scanner;

public class TeacherMenu implements Menu {

    @Override
    public void run(User user, Scanner scanner) {
        Teacher teacher = (Teacher) user;
        DataStorage db = DataStorage.getInstance();

        while (true) {
            System.out.println();
            System.out.println("Teacher menu");
            System.out.println("1. View my courses");
            System.out.println("2. View my students");
            System.out.println("3. Put mark");
            System.out.println("4. View my average rating");
            System.out.println("5. Send complaint to manager");
            System.out.println("0. Logout");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                teacher.logout();
                return;
            } else if (choice.equals("1")) {
                for (Course c : teacher.getCourses()) {
                    System.out.println("  " + c);
                }
            } else if (choice.equals("2")) {
                List<Student> students = teacher.viewStudents();
                if (students.isEmpty()) {
                    System.out.println("  (no students)");
                }
                for (Student s : students) {
                    System.out.println("  " + s.getId() + " " + s.getLogin() + " gpa=" + s.getGpa());
                }
            } else if (choice.equals("3")) {
                System.out.print("Student id: ");
                String sid = scanner.nextLine().trim();
                System.out.print("Course code: ");
                String code = scanner.nextLine().trim();
                System.out.print("Attestation 1 (0-30): ");
                int a1 = parseInt(scanner.nextLine().trim(), 0);
                System.out.print("Attestation 2 (0-30): ");
                int a2 = parseInt(scanner.nextLine().trim(), 0);
                System.out.print("Final exam (0-40): ");
                int fin = parseInt(scanner.nextLine().trim(), 0);
                Student s = (Student) db.findUserById(sid);
                Course c = db.findCourseByCode(code);
                if (s == null || c == null) {
                    System.out.println("Student or course not found");
                } else {
                    try {
                        Mark m = new Mark(sid, code, a1, a2, fin);
                        teacher.putMark(s, c, m);
                        db.addMark(m);
                        System.out.println("Mark recorded: " + m);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Failed: " + e.getMessage());
                    }
                }
            } else if (choice.equals("4")) {
                System.out.println("Average rating: " + teacher.getAverageRating()
                        + " (" + teacher.getRatings().size() + " ratings)");
            } else if (choice.equals("5")) {
                System.out.print("Manager id: ");
                String mid = scanner.nextLine().trim();
                System.out.print("Text: ");
                String text = scanner.nextLine();
                User m = db.findUserById(mid);
                if (m instanceof Manager) {
                    teacher.sendComplaint((Manager) m, text);
                } else {
                    System.out.println("Manager not found");
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
