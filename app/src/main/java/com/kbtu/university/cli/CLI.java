package com.kbtu.university.cli;

import com.kbtu.university.model.user.Admin;
import com.kbtu.university.model.user.Manager;
import com.kbtu.university.model.user.Student;
import com.kbtu.university.model.user.Teacher;
import com.kbtu.university.model.user.User;
import com.kbtu.university.storage.DataStorage;

import java.io.IOException;
import java.util.Scanner;

public class CLI {

    public static final String SAVE_FILE = "university.dat";

    public static void main(String[] args) {
        tryLoadOrSeed();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("KBTU University System");
            System.out.println("1. Login");
            System.out.println("2. Save and exit");
            System.out.println("3. Exit without saving");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                handleLogin(scanner);
            } else if (choice.equals("2")) {
                trySave();
                return;
            } else if (choice.equals("3")) {
                return;
            } else {
                System.out.println("Unknown option");
            }
        }
    }

    private static void tryLoadOrSeed() {
        try {
            DataStorage.load(SAVE_FILE);
            System.out.println("Loaded state from " + SAVE_FILE);
        } catch (IOException | ClassNotFoundException e) {
            Bootstrap.seed();
            System.out.println("Seeded fresh data");
        }
    }

    private static void trySave() {
        try {
            DataStorage.getInstance().save(SAVE_FILE);
            System.out.println("Saved to " + SAVE_FILE);
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    private static void handleLogin(Scanner scanner) {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = DataStorage.getInstance().findUserByLogin(login);
        if (user == null || !user.authenticate(login, password)) {
            System.out.println("Invalid credentials");
            return;
        }

        Menu menu = pickMenu(user);
        if (menu == null) {
            System.out.println("No menu for role " + user.getRole());
            return;
        }
        System.out.println("Welcome, " + user.getLogin() + " (" + user.getRole() + ")");
        menu.run(user, scanner);
    }

    private static Menu pickMenu(User user) {
        if (user instanceof Admin) return new AdminMenu();
        if (user instanceof Manager) return new ManagerMenu();
        if (user instanceof Teacher) return new TeacherMenu();
        if (user instanceof Student) return new StudentMenu();
        return null;
    }
}
