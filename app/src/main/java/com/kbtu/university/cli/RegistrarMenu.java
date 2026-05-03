package com.kbtu.university.cli;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Lesson;
import com.kbtu.university.model.user.RegistrarOfficer;
import com.kbtu.university.model.user.Student;
import com.kbtu.university.model.user.User;
import com.kbtu.university.scheduling.Schedule;
import com.kbtu.university.storage.DataStorage;

import java.util.List;
import java.util.Scanner;

public class RegistrarMenu implements Menu {

    @Override
    public void run(User user, Scanner scanner) {
        RegistrarOfficer registrar = (RegistrarOfficer) user;
        DataStorage db = DataStorage.getInstance();

        while (true) {
            System.out.println();
            System.out.println("Registrar Office menu");
            System.out.println("1. View students by GPA");
            System.out.println("2. View students alphabetically");
            System.out.println("3. Approve registration");
            System.out.println("4. Generate enrollment report");
            System.out.println("5. Generate schedule");
            System.out.println("0. Logout");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                registrar.logout();
                return;
            } else if (choice.equals("1")) {
                printStudents(registrar.viewStudentsByGpa());
            } else if (choice.equals("2")) {
                printStudents(registrar.viewStudentsByLogin());
            } else if (choice.equals("3")) {
                System.out.print("Student id: ");
                String sid = scanner.nextLine().trim();
                System.out.print("Course code: ");
                String code = scanner.nextLine().trim();
                User s = db.findUserById(sid);
                Course c = db.findCourseByCode(code);
                if (s instanceof Student && c != null) {
                    registrar.approveRegistration((Student) s, c);
                    System.out.println("Approved");
                } else {
                    System.out.println("Student or course not found");
                }
            } else if (choice.equals("4")) {
                System.out.println(registrar.generateReport(registrar.viewStudentsByGpa()));
            } else if (choice.equals("5")) {
                printSchedule(registrar.generateSchedule());
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
        for (Lesson l : schedule.getLessons()) {
            String teacherName = l.getTeacher() == null ? "<Vacancy>" : l.getTeacher().getLogin();
            System.out.printf("  %s  %-22s  room=%-5s  teacher=%s%n",
                    l.getDateTime(), l.getCourse().getName(), l.getRoom().getNumber(), teacherName);
        }
        System.out.println("Placed: " + schedule.getLessons().size()
                + ", Vacancies: " + schedule.countVacancies()
                + ", Unplaced: " + schedule.getUnplaced().size());
    }
}
