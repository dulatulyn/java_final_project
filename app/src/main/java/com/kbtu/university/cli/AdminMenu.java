package com.kbtu.university.cli;

import com.kbtu.university.factory.UserFactory;
import com.kbtu.university.model.admin.LogEntry;
import com.kbtu.university.model.enums.ManagerTypeEnum;
import com.kbtu.university.model.enums.TitleEnum;
import com.kbtu.university.model.user.Admin;
import com.kbtu.university.model.user.User;
import com.kbtu.university.storage.DataStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class AdminMenu implements Menu {

    @Override
    public void run(User user, Scanner scanner) {
        Admin admin = (Admin) user;
        DataStorage db = DataStorage.getInstance();

        while (true) {
            System.out.println();
            System.out.println("Admin menu");
            System.out.println("1. List users");
            System.out.println("2. Add student");
            System.out.println("3. Add teacher");
            System.out.println("4. Add manager");
            System.out.println("5. Add registrar");
            System.out.println("6. Add admin");
            System.out.println("7. Remove user by id");
            System.out.println("8. Change user password");
            System.out.println("9. View log");
            System.out.println("10. Save state to file");
            System.out.println("11. Load state from file");
            System.out.println("0. Logout");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                admin.logout();
                return;
            } else if (choice.equals("1")) {
                for (User u : admin.viewUsers()) {
                    System.out.println("  " + u);
                }
            } else if (choice.equals("2")) {
                addStudent(scanner);
            } else if (choice.equals("3")) {
                addTeacher(scanner);
            } else if (choice.equals("4")) {
                addManager(scanner);
            } else if (choice.equals("5")) {
                addRegistrar(scanner);
            } else if (choice.equals("6")) {
                addAdmin(scanner);
            } else if (choice.equals("7")) {
                System.out.print("User id: ");
                String id = scanner.nextLine().trim();
                admin.removeUser(id);
                System.out.println("Removed");
            } else if (choice.equals("8")) {
                System.out.print("User id: ");
                String id = scanner.nextLine().trim();
                System.out.print("New password: ");
                String pw = scanner.nextLine().trim();
                if (admin.changePassword(id, pw)) {
                    System.out.println("Password changed");
                } else {
                    System.out.println("User not found");
                }
            } else if (choice.equals("9")) {
                for (LogEntry entry : admin.viewLog()) {
                    System.out.println("  " + entry);
                }
            } else if (choice.equals("10")) {
                System.out.print("File [university.dat]: ");
                String f = scanner.nextLine().trim();
                if (f.isEmpty()) f = "university.dat";
                try {
                    db.save(f);
                    System.out.println("Saved");
                } catch (IOException e) {
                    System.out.println("Save failed: " + e.getMessage());
                }
            } else if (choice.equals("11")) {
                System.out.print("File [university.dat]: ");
                String f = scanner.nextLine().trim();
                if (f.isEmpty()) f = "university.dat";
                try {
                    DataStorage.load(f);
                    System.out.println("Loaded");
                    return;
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Load failed: " + e.getMessage());
                }
            } else {
                System.out.println("Unknown option");
            }
        }
    }

    private void addStudent(Scanner scanner) {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Major: ");
        String major = scanner.nextLine().trim();
        System.out.print("Year (1-7): ");
        int year = parseInt(scanner.nextLine().trim(), 1);
        try {
            UserFactory.createStudent(login, password, major, year);
            System.out.println("Created");
        } catch (RuntimeException e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }

    private void addTeacher(Scanner scanner) {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Salary: ");
        double salary = parseDouble(scanner.nextLine().trim(), 0);
        System.out.print("School: ");
        String school = scanner.nextLine().trim();
        TitleEnum title = pickTitle(scanner);
        try {
            UserFactory.createTeacher(login, password, salary, LocalDate.now(), school, title);
            System.out.println("Created");
        } catch (RuntimeException e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }

    private void addManager(Scanner scanner) {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Salary: ");
        double salary = parseDouble(scanner.nextLine().trim(), 0);
        System.out.print("School: ");
        String school = scanner.nextLine().trim();
        TitleEnum title = pickTitle(scanner);
        ManagerTypeEnum type = pickManagerType(scanner);
        try {
            UserFactory.createManager(login, password, salary, LocalDate.now(), school, title, type);
            System.out.println("Created");
        } catch (RuntimeException e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }

    private void addRegistrar(Scanner scanner) {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Salary: ");
        double salary = parseDouble(scanner.nextLine().trim(), 0);
        System.out.print("School: ");
        String school = scanner.nextLine().trim();
        try {
            UserFactory.createRegistrar(login, password, salary, LocalDate.now(), school);
            System.out.println("Created");
        } catch (RuntimeException e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }

    private void addAdmin(Scanner scanner) {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Salary: ");
        double salary = parseDouble(scanner.nextLine().trim(), 0);
        System.out.print("School: ");
        String school = scanner.nextLine().trim();
        try {
            UserFactory.createAdmin(login, password, salary, LocalDate.now(), school);
            System.out.println("Created");
        } catch (RuntimeException e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }

    private TitleEnum pickTitle(Scanner scanner) {
        System.out.println("Title: 1=TUTOR 2=LECTOR 3=SENIOR_LECTOR 4=ASSOCIATE_PROFESSOR 5=PROFESSOR");
        System.out.print("> ");
        int t = parseInt(scanner.nextLine().trim(), 1);
        TitleEnum[] all = TitleEnum.values();
        if (t < 1 || t > all.length) return TitleEnum.TUTOR;
        return all[t - 1];
    }

    private ManagerTypeEnum pickManagerType(Scanner scanner) {
        System.out.println("Type: 1=DEPARTMENT 2=DEAN 3=RECTOR");
        System.out.print("> ");
        int t = parseInt(scanner.nextLine().trim(), 1);
        ManagerTypeEnum[] all = ManagerTypeEnum.values();
        if (t < 1 || t > all.length) return ManagerTypeEnum.DEPARTMENT;
        return all[t - 1];
    }

    private static int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static double parseDouble(String s, double fallback) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
